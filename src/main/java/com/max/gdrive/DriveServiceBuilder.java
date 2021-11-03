package com.max.gdrive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.max.gdrive.utils.DeveloperConstants;

public class DriveServiceBuilder {

	private String refreshToken = "";
	private static final Logger logger = LoggerFactory.getLogger(DriveServiceBuilder.class);

	public DriveServiceBuilder(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Drive createService() throws Exception {
		int retry = 0;
		Credential credential = null;
		Drive driveService = null;

		while (retry <= DeveloperConstants.MAX_RETRY) {
			try {
				credential = new GoogleCredential.Builder()
						.setTransport(new NetHttpTransport())
						.setJsonFactory(new JacksonFactory())
						.setClientSecrets(DeveloperConstants.CLIENT_ID,DeveloperConstants.CLIENT_SECRET)
						.build();

				credential.setRefreshToken(this.refreshToken);

				driveService = new Drive
						.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
						.setApplicationName("KW_DRIVE")
						.build();

				return driveService;
			} catch (Exception sessionError) {
				if (retry == DeveloperConstants.MAX_RETRY) {
					throw new Exception(sessionError);
				}
				logger.info("Session Error (retry in "+DeveloperConstants.SLEEP_TIME+"): "+sessionError);
				Thread.sleep(DeveloperConstants.SLEEP_TIME);
			}
			retry++;
		}

		return driveService;
	}

}
