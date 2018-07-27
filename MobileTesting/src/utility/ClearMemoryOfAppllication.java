package utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClearMemoryOfAppllication {
	
	public static void clearAppMemory(String appPackagename) {
		String command = "shell pm clear "+appPackagename;
		executeAdbCommand(command);
	}

	public static String executeAdbCommand(String command) {
		String output = "";
		String line = "";
		String adbPath;
		Runtime run = Runtime.getRuntime();
		Process process = null;



		adbPath = System.getenv("ANDROID_HOME") + "/platform-tools/adb ";


		try {

			process = run.exec("C://Users//nkadam//AppData//Local//Android//sdk//platform-tools//adb " + command);


			process.waitFor();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = buffer.readLine()) != null) {
				output = output + " " + line;
			}
			buffer.close();
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		catch (InterruptedException e) {

			e.printStackTrace();
		}

		return output;
	}

}
