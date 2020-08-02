package com.danielbulger.workflow.repository.component;

import com.danielbulger.workflow.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Port(
	name = "url",
	valueType = String.class,
	type = PortType.INPUT
)
@Port(
	name = "output",
	valueType = String.class,
	type = PortType.OUTPUT
)
@Port(
	name = "error",
	valueType = Exception.class,
	type = PortType.OUTPUT
)
public class HttpComponent extends Component {

	public HttpComponent(Network network) {
		super(network);
	}

	@Override
	public void execute() {

		final Buffer<String> urlBuffer = super.getBuffer("url");

		if (urlBuffer.isEmpty()) {
			return;
		}

		final String url = urlBuffer.take();

		final HttpClient client = HttpClient.newBuilder()
			.followRedirects(HttpClient.Redirect.NORMAL)
			.build();

		final HttpRequest request = HttpRequest.newBuilder()
			.uri(URI.create(url))
			.build();

		try {
			final HttpResponse<String> response = client.send(
				request, HttpResponse.BodyHandlers.ofString()
			);

			final String body = response.body();

			if(body.length() > 0) {
				super.send("output", body);
			}

		} catch (Exception exception) {
			super.send("error", exception);
			exception.printStackTrace();
		}
	}
}
