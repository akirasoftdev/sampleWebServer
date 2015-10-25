import static org.junit.Assert.*;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class WorkerServletTest {
	
	@Before
	public void setup() {
		System.setProperty("RUN_MODE",  "UNIT_TEST");
	}
	
	private AmazonS3Client getMockedS3Client() {
		return S3Factory.getS3Client(System.getProperty("RUN_MODE"));
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws ServletException, IOException {
		
		WorkerServlet servlet = new WorkerServlet();
		String requestBody = "{\"bucket\":\"this_is_bucket\",\"key\":\"this_is_key\",\"message\":\"this_is_message\"}";
		ServletInputStream sis = createServletInputStream(requestBody, "utf-8");
		
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		when(request.getInputStream()).thenReturn(sis);
		StringWriter out = new StringWriter();
		when(response.getWriter()).thenReturn(new PrintWriter(out));

		AmazonS3Client client = getMockedS3Client();
		when(client.putObject(anyString(), anyString(), any(InputStream.class), any(ObjectMetadata.class))).thenReturn(null);
		
		servlet.doPost(request, response);
	}

	public static ServletInputStream createServletInputStream(String s, String charset) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
	        baos.write(s.getBytes(charset));
	    } catch (Exception e) {
	        throw new RuntimeException("No support charset.");
	    }

	    final InputStream bais = new ByteArrayInputStream(baos.toByteArray());

	    return new ServletInputStream() {

	        @Override
	        public int read() throws IOException {
	            return bais.read();
	        }
	    };
	}
}
