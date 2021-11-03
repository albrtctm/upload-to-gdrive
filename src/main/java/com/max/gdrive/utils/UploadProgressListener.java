package com.max.gdrive.utils;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;

public class UploadProgressListener implements MediaHttpUploaderProgressListener {

	private static final Logger logger = LoggerFactory.getLogger(UploadProgressListener.class);

	public void progressChanged(MediaHttpUploader uploader) throws IOException {
		switch (uploader.getUploadState()) {
		case INITIATION_STARTED:
			logger.info("Initiation Started");
			break;
		case INITIATION_COMPLETE:
			logger.info("Initiation Completed");
			break;
		case MEDIA_IN_PROGRESS:
			logger.info("Upload in progress: " + (uploader.getProgress()*100) +"%");
			break;
		case MEDIA_COMPLETE:
			logger.info("Upload Completed!");
			break;
		default:
			break;
		}
	}
}