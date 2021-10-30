package in.cs699.tensors.delagram;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import in.cs699.tensors.delagram.content_view_service.service.ContentViewServiceImpl;

/**
 * A class to write-up test cases
 * @author swaroop_nath, mayankkakad
 *
 */
@SpringBootTest
class DelagramApplicationTests {
	
	@Autowired ContentViewServiceImpl contentViewService;

	@Test
	void contextLoads() {
	}
}
