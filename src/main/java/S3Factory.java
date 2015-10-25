import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3Factory {
	private static S3Factory instance = null;
	private AmazonS3Client s3 = null;
	
	private S3Factory() {
	}
	
	public static S3Factory getInstance() {
		synchronized (S3Factory.class) {
			if (instance != null) {
				return instance;
			}
			instance = new S3Factory();
		}
		return instance;
	}
	
	public synchronized AmazonS3Client getS3Client() {
		if (s3 != null) {
			return s3;
		}
		s3 = new AmazonS3Client(
				new AWSCredentialsProviderChain(
						new InstanceProfileCredentialsProvider(),
						new ClasspathPropertiesFileCredentialsProvider()));
		return s3;
	}
}
