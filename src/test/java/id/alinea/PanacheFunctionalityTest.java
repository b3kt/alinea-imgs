package id.alinea;

import id.alinea.imgs.entity.SystemParameter;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;

import javax.ws.rs.WebApplicationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class PanacheFunctionalityTest {

	@Test
	public void testPanacheMocking() {
		PanacheMock.mock(SystemParameter.class);

		// Mocked classes always return a default value
		Assertions.assertEquals(0, SystemParameter.count());

		// Now let's specify the return value
		Mockito.when(SystemParameter.count()).thenReturn(23L);
		Assertions.assertEquals(23, SystemParameter.count());

		// Now let's change the return value
		Mockito.when(SystemParameter.count()).thenReturn(42L);
		Assertions.assertEquals(42, SystemParameter.count());

		// Now let's call the original method
		Mockito.when(SystemParameter.count()).thenCallRealMethod();
		Assertions.assertEquals(2, SystemParameter.count());

		// Check that we called it 4 times
//        PanacheMock.verify(SystemParameter.class, Mockito.times(4)).count();

		// Mock only with specific parameters
		SystemParameter p = new SystemParameter();
		Mockito.when(SystemParameter.findById(12L)).thenReturn(p);
		Assertions.assertSame(p, SystemParameter.findById(12L));
		Assertions.assertNull(SystemParameter.findById(42L));

		// Mock throwing
		Mockito.when(SystemParameter.findById(12L)).thenThrow(new WebApplicationException());
		Assertions.assertThrows(WebApplicationException.class, () -> SystemParameter.findById(12L));

		// We can even mock your custom methods
		// Mockito.when(SystemParameter.findOrdered()).thenReturn(Collections.emptyList());
		// Assertions.assertTrue(SystemParameter.findOrdered().isEmpty());

		// Mocking a void method
		// SystemParameter.voidMethod();

		// Make it throw
		// PanacheMock.doThrow(new
		// RuntimeException("Stef2")).when(SystemParameter.class).voidMethod();
		// try {
		// SystemParameter.voidMethod();
		// Assertions.fail();
		// } catch (RuntimeException x) {
		// Assertions.assertEquals("Stef2", x.getMessage());
		// }

		// Back to doNothing
		// PanacheMock.doNothing().when(SystemParameter.class).voidMethod();
		// SystemParameter.voidMethod();

		// Make it call the real method
		// PanacheMock.doCallRealMethod().when(SystemParameter.class).voidMethod();
		// try {
		// SystemParameter.voidMethod();
		// Assertions.fail();
		// } catch (RuntimeException x) {
		// Assertions.assertEquals("void", x.getMessage());
		// }

		// PanacheMock.verify(SystemParameter.class).findOrdered();
		// PanacheMock.verify(SystemParameter.class, Mockito.atLeast(4)).voidMethod();
//        PanacheMock.verify(SystemParameter.class, Mockito.atLeastOnce()).findById(Mockito.any());
		PanacheMock.verifyNoMoreInteractions(SystemParameter.class);
	}

}
