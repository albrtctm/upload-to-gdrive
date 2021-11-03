package com.max.gdrive;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class GoogleDriverServiceTest {

	private static Drive driveService;
	private GoogleDriverService googleDriveService;
	private static String REFRESHTOKEN = "[ADD REFRESH TOKEN]";
	private static String FILECREATE = "src/test/resources/test-temp.tsv";
	private static String FILEUPDATE = "src/test/resources/test.tsv";
	private static String FILETYPE = "text/tsv";

	@BeforeClass
	public static void beforeClass(){
		try {
			driveService = new DriveServiceBuilder(REFRESHTOKEN).createService();
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Before
	public void before(){
		googleDriveService = new GoogleDriverService(driveService);
	}

	@Test
	public void googleDriveApiTest() {
		try {
			List<File> files = googleDriveService.getFiles();
			assertNotNull(files);
			assertTrue(files.size() > 0);
			assertNotNull(files.get(0).getName());
			assertNotNull(files.get(0).getId());

			File file = googleDriveService.getFileById(files.get(0).getId());
			assertNotNull(file.getName());
			assertNotNull(file.getId());

			File fileCreated = googleDriveService.uploadFile(FILECREATE, FILETYPE, false);
			assertNotNull(fileCreated.getName());
			assertNotNull(fileCreated.getId());

			File fileUpdate = googleDriveService.uploadToUpdateFile(fileCreated.getId(), FILEUPDATE, FILETYPE, false);
			assertNotNull(fileUpdate.getName());
			assertNotNull(fileUpdate.getId());

			assertTrue(googleDriveService.deleteFileById(fileUpdate.getId()));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
}