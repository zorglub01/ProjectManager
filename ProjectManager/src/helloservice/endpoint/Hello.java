package helloservice.endpoint;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name="TestWS")
@SOAPBinding
(
	style = SOAPBinding.Style.DOCUMENT,
	use = SOAPBinding.Use.LITERAL,
	parameterStyle = SOAPBinding.ParameterStyle.WRAPPED
)
public class Hello {

	private String message = new String("Hello:");

	public Hello() {
		// TODO Auto-generated constructor stub
	}
	
	@WebMethod
	public String sayHello(String _name){
		return message  + _name;
	}

}
