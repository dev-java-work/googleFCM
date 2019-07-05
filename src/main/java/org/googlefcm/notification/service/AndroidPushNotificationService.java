package org.googlefcm.notification.service;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidPushNotificationService {
	
	private static final String FIREBASE_SERVER_KEY= "AAAAyqfShl0:APA91bHZywZDSGlhuC1pafQCQBPYU1UZbST5eG8kPmwlOF5r9OsZCM6pAnhhAjGnRDTMMIsExfimxcXvIOQkwPhJ43C5ihwma7IZ0usNVJ39A0rYlwxO23N3mYYweTZ7uIzGdA97j9qu";
	private static final String FIREBASE_API_URL="https://fcm.googleapis.com/fcm/send";
	
	@Async
	public CompletableFuture<String> send(HttpEntity<String> entity){
		
		RestTemplate restTemplate = new RestTemplate();
		
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key="+FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
		restTemplate.setInterceptors(interceptors);
		
		String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
		
		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
