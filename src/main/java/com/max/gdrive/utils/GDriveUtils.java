package com.max.gdrive.utils;

import java.util.List;

import com.google.api.services.drive.model.File;

public class GDriveUtils {
	public static String fileExist(String fileName, List<File> files) {
		for (File file : files) {
			if(fileName.equalsIgnoreCase(file.getName())) return file.getId();
		}
		return null;
	}
}