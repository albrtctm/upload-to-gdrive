package com.max.main;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.max.gdrive.DriveServiceBuilder;
import com.max.gdrive.GoogleDriverService;
import com.max.gdrive.utils.GDriveUtils;

public class GDriveMainRunner {

	private static final Logger logger = LoggerFactory.getLogger(GDriveMainRunner.class);

	public static void main(String[] params) {
		try {

			switch (params.length) {
			case 1:
				System.out.print("Missing file type & file path param.\n"
						+ "e.g java -jar <JAR FILE> <REFRESH TOKEN> <FILE TYPE> <FILE PATH>");
				System.exit(0);
				break;
			case 2:
				System.out.print("Missing file path param.\n"
						+ "e.g java -jar <JAR FILE> <REFRESH TOKEN> <FILE TYPE> <FILE PATH>");
				System.exit(0);
				break;
			case 0:
				System.out.print("Missing refreshtoken, file type & file path params.\n"
						+ "e.g java -jar <JAR FILE> <REFRESH TOKEN> <FILE TYPE> <FILE PATH>");
				System.exit(0);
				break;
			default:
				break;
			}

			String refreshToken = params[0];
			String fileType = params[1];
			String filePath = params[2];
			boolean useDirectUpload = false;

			Drive driveService = new DriveServiceBuilder(refreshToken).createService();
			GoogleDriverService googleDriveService = new GoogleDriverService(driveService);

			java.io.File uploadFile = new java.io.File(filePath);
			String fileId = googleDriveService.getFileIdByName(uploadFile.getName());
			if(fileId != null){
				File file = googleDriveService.uploadToUpdateFile(fileId, filePath, fileType, useDirectUpload);
				logger.info(file.getName()+ " has been uploaded sucessfully.");
			}else{
				File file = googleDriveService.uploadFile(filePath, fileType, useDirectUpload);
				logger.info(file.getName()+ " has been uploaded sucessfully.");
		}

		} catch (Exception e) {
			logger.error("Error while running the CLI app: "+e.toString());
		}
	}

}
