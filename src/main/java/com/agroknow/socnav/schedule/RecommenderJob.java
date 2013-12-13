package com.agroknow.socnav.schedule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.agroknow.socnav.domain.Item;
import com.agroknow.socnav.domain.Rating;
import com.agroknow.socnav.domain.Recommendation;
import com.agroknow.socnav.domain.User;
import com.agroknow.socnav.service.RecommendationService;

@Service
public class RecommenderJob {
	
	@Autowired
	ServletContext context;
	
	@Autowired
    RecommendationService recommendationService;
	
	@Value("${recommenderJob.fileName}")
	private String fileName;
	
	@Value("${recommenderJob.regServiceUrl}")
	private String regServiceUrl;
	
	@Value("${recommenderJob.outputServiceUrl}")
	private String outputServiceUrl;
	
	@Value("${recommenderJob.username}")
	private String username;
	
	@Value("${recommenderJob.password}")
	private String password;

	@Value("${recommenderJob.params}")
	private String recParams;
	
	private DefaultHttpClient client = new DefaultHttpClient();

	@Value("${recommenderJob.serverUrl}")
	private String serverUrl;
	
    private static final Logger log = LoggerFactory.getLogger(RecommenderJob.class);
	

    //The schedule cron job
    @Scheduled(cron="${recommenderJob.cronExpression}")
	public void perform() {
		log.debug("Run recommender job");
		log.debug("Step 1: create dataset file");
		try {
			createDatasetFile();
		} catch (IOException e) {
			log.error("Cannot create dataset file, abort!");
			e.printStackTrace();
			return;
		}
		
		log.debug("Step 2: register recommender input");
		String regId = null;
		try {
			regId = registerRecommenderInput();
			log.debug("Registration id: "+regId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (regId == null) {
				client.getConnectionManager().shutdown();
				log.error("New dataset registration failed. No regId.");
				return;
			}
		}
		
		String outputUrl = null;
		int repeat = 0;
		while (outputUrl == null){
			repeat++;
			try {
				log.debug("Wait for 50000 mils");
				Thread.sleep(50000);
				outputUrl = getOutputUrl(regId);
			} catch (Exception e) {
				client.getConnectionManager().shutdown();
				e.printStackTrace();
			}
			
			if (repeat > 10){
				log.error("After 10 repeat calls, nothing... It s time to stop");
				return;
			}
		}
		
		log.debug("Step 3: get recommendations from : "+outputUrl);
		try {
			getRecommendations(outputUrl);
		} catch (Exception e) {
			log.error("Error while cosuming data");
			e.printStackTrace();
			client.getConnectionManager().shutdown();
			return;
		}
		
		log.debug("Finish recommender cron job succefully");
		client.getConnectionManager().shutdown();
	}
    
    //Step 1
    private void createDatasetFile() throws IOException {
		List<Rating> ratings = Rating.findAllRatings();
		String path = context.getRealPath(this.fileName);
		
		FileWriter fw = new FileWriter(path);
		BufferedWriter bf = new BufferedWriter(fw);
		
		for (Rating rating : ratings) {
			ArrayList<String> arr = new ArrayList<String>();
			arr.add(rating.getUser().getId().toString());
			arr.add(rating.getItem().getId().toString());
			Float f = rating.getPreference_avg();				
			arr.add(f.toString());
			// example 1,1,4.5
			String jString = arr.toString().replace("[", "");
			jString = jString.replace("]", "");
			jString = jString.replace(" ", "");
			bf.write(jString);
			bf.newLine();
		}
		bf.flush();
		bf.close();
	}
    
    //Step 2
    private String registerRecommenderInput() throws ClientProtocolException, IOException, JSONException {
		
		String auth = this.username+":"+this.password;
		String encoding = Base64.encodeBase64String(auth.getBytes());
		
		HttpPost post = new HttpPost(this.regServiceUrl);
		post.setHeader("Authorization", "Basic " + encoding);
		JSONObject jParams = new JSONObject();

		JSONObject jRecommendObj = new JSONObject();
		jRecommendObj.put("type", "rcm_in");
		jRecommendObj.put("process", "agupload");
		
		// for testing
		//String datasetPath = "https://dl.dropboxusercontent.com/u/91188863/ratings.dat";
		String datasetPath = this.serverUrl+context.getContextPath()+"/resources"+this.fileName;
		jRecommendObj.put("ext_dataset_location", datasetPath);
		
		JSONArray jArr = new JSONArray();
		jArr.put(recParams);
		jRecommendObj.put("rcm_pars", jArr);
		
		jParams.put("document_type", "recommend");
		jParams.put("recommend", jRecommendObj);

		
		StringEntity reqEntity = null;
		reqEntity = new StringEntity(jParams.toString());
		post.setEntity(reqEntity);
		log.debug("Post: "+post.getRequestLine().toString()+" , "+post.getEntity());
		HttpResponse response = client.execute(post);
		ResponseHandler<String> handler = new BasicResponseHandler();

		String body = handler.handleResponse(response);
		log.debug(body);
		JSONObject jResponse=new JSONObject(body);
		String regId = jResponse.getString("id");
		
		return regId;
	}
	
    //Step 3
	private String getOutputUrl(String regId) throws ClientProtocolException, IOException {
		
		HttpGet get = new HttpGet(this.outputServiceUrl+regId);
		HttpResponse response = client.execute(get);
		
		ResponseHandler<String> getHandler = new BasicResponseHandler();

		String body = getHandler.handleResponse(response);
		String outputUrl = null;
		try {
			JSONObject getJResponse = new JSONObject(body);
			//if there is row then the task has been finished
			JSONObject row = getJResponse.getJSONArray("rows").getJSONObject(0);
			outputUrl = row.getJSONObject("value").getJSONObject("recommend").getString("http_dataset_location");
		} catch (JSONException e) {
			log.debug("Task not finished");
			log.debug(e.getMessage());
			return null;
		}
		
		return outputUrl;
	}
	
	//Step 4
	private void getRecommendations(String outputUrl) throws ClientProtocolException, IOException, JSONException {
		
		//for testing
		//HttpGet get = new HttpGet("http://agro.ipb.ac.rs/datasets/b306412372f3ad335013f433498f8713.tgz");		
		HttpGet get = new HttpGet(outputUrl);
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		
		if (entity != null){
			InputStream instream = entity.getContent();
			TarArchiveInputStream tarInput = new TarArchiveInputStream(new GZIPInputStream(instream));
			
			TarArchiveEntry dirEntry = tarInput.getNextTarEntry();
			//Only one file should be there, since there is only one command in params
			TarArchiveEntry fileEntry = tarInput.getNextTarEntry();
			if (fileEntry.isFile()){
				String filename = fileEntry.getName();
				byte[] content = new byte[(int) fileEntry.getSize()];
				int offset=0;
				tarInput.read(content, offset, content.length - offset);
				File f = new File(context.getRealPath("/datasets/out.txt"));
				FileOutputStream outputFile = new FileOutputStream(f);
				/* Use IOUtiles to write content of byte array to physical file */
                IOUtils.write(content,outputFile);              
                /* Close Output Stream */
                outputFile.close();
                tarInput.close();
                
                Recommendation.deleteAll();
                BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				while ((line = br.readLine()) != null) {
				   String[] args = line.split(",");
				   Long userId = Long.valueOf(args[0]);
				   Long itemId = Long.valueOf(args[1]);
				   Float eval = Float.valueOf(args[2]);
				   log.debug("line: "+userId+" - "+itemId+" - "+eval);
				   
				   User user = User.findUser(userId);
				   Item item = Item.findItem(itemId);
				   
				   if (user!=null && item!=null){
					   Recommendation recommendation = new Recommendation();
					   recommendation.setUser(user);
					   recommendation.setItem(item);
					   recommendation.setEvaluation(eval);
					   recommendationService.saveRecommendation(recommendation);
				   }
				   
				}
				br.close();
			}else {
				log.error("Recommendations file not found in tar");
				tarInput.close();
			}
		}
	}
}