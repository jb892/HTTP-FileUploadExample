import java.io.IOException;

public class main {

	public static void main(String[] args) throws IOException, InterruptedException {
		
		// Id and keys required for identity authentication.
		String app_id = "85097c39a82a414abf516601a53ca949";
		String app_key = "cfbc1b58738041c79dcc40e9ca17402c";
		
		// Invoke getToken function to get a unique token.
		String token = HttpUtil.getToken(app_id, app_key);
		System.out.println("Token = " + token);
		
		// Full Model path:
		String filePath = "/home/jack/eclipse-workspace/FileUploadExample/models/test.obj";
		
		// Invoke upload function
		String path = HttpUtil.upload(token, filePath);
		
		System.out.println("Path = " + path);
		
		// Invoke getStatus function
		String status = HttpUtil.getStatus(token, path);
		System.out.println("Status = " + status);

	}

}
