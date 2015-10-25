import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import static org.mockito.Mockito.*;

public class S3Factory {
	private static AmazonS3Client s3 = null;
	
	public static synchronized AmazonS3Client getS3Client(String runMode) {
		if (s3 != null) {
			return s3;
		}
		if ("UNIT_TEST".equalsIgnoreCase(runMode)) {
			s3 = mock(AmazonS3Client.class);
			return s3;
			
		} else {
			s3 = new AmazonS3Client(
					new AWSCredentialsProviderChain(
							new InstanceProfileCredentialsProvider(),
							new ClasspathPropertiesFileCredentialsProvider()));
			return s3;
		}
	}
}
