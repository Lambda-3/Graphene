package org.lambda3.graphene.core.relation_extraction.complex_categories;


import com.typesafe.config.Config;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ComplexCategoryExtractor {

	private static final String URL_PROPERTY_NAME = "complex.category.extractor.url";

	private static final String USER_AGENT = "Linse Query Parser 0.2.0";
	private static final String DEFAULT_URL = "http://127.0.0.1:5000/";
	private static final String DIRECT_SPEC = "spec";

	private String url;

	public ComplexCategoryExtractor() {
		this(null);
	}

	public ComplexCategoryExtractor(Config config) {
		if (config != null) {
			this.url = config.getString(URL_PROPERTY_NAME);
		} else {
			this.url = System.getProperty(URL_PROPERTY_NAME, DEFAULT_URL);
		}
	}

	private JSONObject getQueryJson(String query) throws Exception {

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// add request header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Content-Type", "application/json; charset=utf8");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes("{\"names\":[\"" + query + "\"]}");
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();

		if (responseCode == 200) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			return new JSONObject(response.toString());

		} else {
			throw new RuntimeException("response code: " + responseCode);
		}
	}

	public ComplexCategory getComplexCategory(String query) {
		try {
			JSONObject json = getQueryJson(query.toLowerCase());
			List<ComplexCategory> yclasses = getComplexCategories(json);
			return (yclasses.isEmpty() ? null : yclasses.get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<ComplexCategory> getComplexCategories(File jsonInFile) {

		JSONObject jsonObject = null;

		StringBuilder jsonString = new StringBuilder();
		try (FileInputStream in = new FileInputStream(jsonInFile)) {

			byte[] bytes = new byte[10240];
			while (in.read(bytes) != -1) {
				jsonString.append(new String(bytes, StandardCharsets.UTF_8));
			}

			jsonObject = new JSONObject(jsonString.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
		// DO Nothing

		return (jsonObject != null ? getComplexCategories(jsonObject) : Collections.EMPTY_LIST);
	}

	private List<ComplexCategory> getComplexCategories(JSONObject json) {
		ComplexCategory cc = null;

		JSONArray jsonArray = json.getJSONArray("data");
		List<ComplexCategory> descriptors = new LinkedList<>();

		if (jsonArray != null) {
			JSONObject jsonObject;

			for (int j = 0; j < jsonArray.length(); j++) {
				jsonObject = jsonArray.getJSONObject(j);

				Chunk core = getChunk(jsonObject.getJSONObject("core"));
				cc = new ComplexCategory(jsonObject.getString("name"), core);

				descriptors.add(cc);
			}
		}

		return (descriptors.isEmpty() ? Collections.EMPTY_LIST : descriptors);
	}

	private static Chunk getChunk(JSONObject jsonObject) {
		String[] specTypes = null;
		Chunk[] specContents = null;

		JSONArray specs = jsonObject.getJSONArray("specs");
		if (specs.length() > 0) {
			specTypes = new String[specs.length()];
			specContents = new Chunk[specs.length()];
			JSONObject spec;

			for (int i = 0; i < specs.length(); i++) {
				spec = specs.getJSONObject(i);
				specTypes[i] = spec.getString("type");
				specContents[i] = getChunk(spec.getJSONObject("content"));
			}
		}
		String pureTerm = jsonObject.getString("pure_term");
		String effectiveTerm = jsonObject.getString("effective_term");
		String tag = jsonObject.getString("tag");

		return new Chunk(pureTerm, effectiveTerm, tag, specTypes, specContents);
	}
}
