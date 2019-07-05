package org.googlefcm.notification.controller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.googlefcm.notification.service.AndroidPushNotificationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

	private final String TOPIC="JavaSampleApproach";
	
	@Autowired
	AndroidPushNotificationService androidPushNotificationService;
	
	@RequestMapping(value="/send", method=RequestMethod.GET, produces = "application/json")
	public ResponseEntity<String> send(){
	
	System.out.println("within send method........");
	JSONObject body = new JSONObject();
	body.put("to", "/topics/"+TOPIC);
	body.put("priority", "high");
	
    JSONObject notification = new JSONObject();
    notification.put("title", "JSA Notification");
    notification.put("body", "Happy Message!");
    
    JSONObject data = new JSONObject();
    data.put("Key-1", "JSA Data 1");
    data.put("Key-2", "JSA Data 2");
 
    body.put("notification", notification);
    body.put("data", data);
    
    HttpEntity<String> request = new HttpEntity<>(body.toString());
    
    CompletableFuture<String> pushNotification = androidPushNotificationService.send(request);
    CompletableFuture.allOf(pushNotification).join();
    
    try {
    	String firebaseResponse = pushNotification.get();
    	return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
    }catch(InterruptedException | ExecutionException e) {
    	e.printStackTrace();
    }
	
    return new ResponseEntity<>("Push Notification Error!", HttpStatus.BAD_REQUEST);
	}
}
