package org.sample.utils;


import org.newSite.common.dean.RunCmdResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


public class RunCmdUtil {

	public static RunCmdResult executeLinuxCmd(String cmd) {
		return executeCmd("/bin/bash", "-c", cmd);
	}

	public static RunCmdResult executeWindowsCmd2(String cmd) {
		return executeCmd("cmd.exe", "/c", cmd);
	}

	private static RunCmdResult executeCmd(String... cmd) {
		RunCmdResult cmdResult = new RunCmdResult();

		ProcessBuilder builder = new ProcessBuilder();
		builder.redirectErrorStream(true);
		builder.command(cmd);

		try {
			Process process = builder.start();

			String ret;
			try (InputStream in = process.getInputStream()) {
				ret = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
			}
			cmdResult.setResult(ret);

			int exitCode = process.waitFor();
			cmdResult.setExitCode(exitCode);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			cmdResult.setExitCode(1);
		}

		return cmdResult;
	}

	public static void main(String[] args) {
		//curl ftp://10.37.153.180/sit/phoebus/1b514d0517bbd199c22a8623b3ab0c46/phoebus-web-20200218183249.war -o E:\suning\3
		//curl ftp://10.37.153.180/sit/phoebus/1b514d0517bbd199c22a8623b3ab0c46/
		
		String parentPath = "ftp://10.37.153.180/sit/phoebus/625bf83a78a783919923e14afbb08303/";
		
		String cmd = "curl " + parentPath;
		
		RunCmdResult cmdResult = executeWindowsCmd2(cmd);
		
		System.out.println(cmdResult.getResult());
		
		String[] results = cmdResult.getResult().split("\n");
		
		for (String rowInfo : results) {
			if (!rowInfo.startsWith("-rw-r--r--")) {
				continue;
			}
			
			String[] rowInfos = rowInfo.split(" ");
			
			System.err.println(rowInfos[rowInfos.length-1]);
			
		}
		
	}
	
}
