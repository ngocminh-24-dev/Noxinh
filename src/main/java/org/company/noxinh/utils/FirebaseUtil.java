package org.company.noxinh.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class FirebaseUtil {

    private final Environment env;

    @Getter
    private static StorageClient storageClient;

    @PostConstruct
    private void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                String path = env.getProperty("firebase.credentials.path") != null ? env.getProperty("firebase.credentials.path") : env.getProperty("FIREBASE_CREDENTIALS_PATH");
                if (path == null) {
                    throw new RuntimeException("Firebase credentials path is not set.");
                }
                FileInputStream serviceAccount = new FileInputStream(path);
                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
            storageClient = StorageClient.getInstance(FirebaseApp.getInstance());
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }
}