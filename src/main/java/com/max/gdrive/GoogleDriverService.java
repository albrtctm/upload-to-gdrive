package com.max.gdrive;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Create;
import com.google.api.services.drive.Drive.Files.Update;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.max.gdrive.utils.UploadProgressListener;

public class GoogleDriverService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleDriverService.class);
	private Drive drive = null;

	public GoogleDriverService(Drive drive) {
		this.drive = drive;
	}

	public File getFileById(String id) {
		File file = null; 
		try {
			file = drive.files().get(id).execute();
			return file;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return file;
	}

	public String getFileIdByName(String name) {

		String q = new StringBuilder("name='")
				.append(name.replace("'","\\'"))
				.append("'")
				.toString();
		String pageToken = null;
		do {
			try {
				FileList result = drive.files().list()
						.setQ(q)
						.setSpaces("drive")
						.setFields("nextPageToken, files(id, name)")
						.setPageToken(pageToken)
						.execute();
				for(File file: result.getFiles()) {
					System.out.printf("Found file: %s (%s)\n",
							file.getName(), file.getId());
					return file.getId();
				}
				pageToken = result.getNextPageToken();
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				pageToken = null;
			}
		} while (pageToken != null);
		return null;
	}

	public List<File> getFiles() throws Exception {
		List<File> result = new ArrayList<File>();
	    Files.List request = drive.files().list();

	    do {
	      try {
	        FileList files = request.execute();

	        result.addAll(files.getFiles());
	        request.setPageToken(files.getNextPageToken());
	      } catch (IOException e) {
	        System.out.println("An error occurred: " + e);
	        request.setPageToken(null);
	      }
	    } while (request.getPageToken() != null &&
	             request.getPageToken().length() > 0);

	    return result;
	}

	public File uploadFile(String fileNamePath, String fileType, boolean useDirectUpload) throws IOException {
		File fileMetadata = new File();
		java.io.File uploadFile = new java.io.File(fileNamePath);
		fileMetadata.setName(uploadFile.getName());

		FileContent fileContent = new FileContent(fileType, uploadFile);

		Create insert = drive.files().create(fileMetadata, fileContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		uploader.setProgressListener(new UploadProgressListener());
		return insert.execute();
	}

	public File uploadToUpdateFile(String id, String fileNamePath, String fileType, boolean useDirectUpload) throws IOException {
		File fileMetadata = new File();
		java.io.File uploadFile = new java.io.File(fileNamePath);
		fileMetadata.setName(uploadFile.getName());

		FileContent fileContent = new FileContent(fileType, uploadFile);

		Update insert = drive.files().update(id, fileMetadata, fileContent);
		MediaHttpUploader uploader = insert.getMediaHttpUploader();
		uploader.setDirectUploadEnabled(useDirectUpload);
		uploader.setProgressListener(new UploadProgressListener());
		return insert.execute();
	}

	public boolean deleteFileById(String id) {
		try {
			drive.files().delete(id).execute();
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
