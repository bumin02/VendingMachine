package SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class JsonParser {

	public static ArrayList<Card> parseCreditCardFile() {

		JSONParser parser = new JSONParser();

		try {

			String currentDirectory = System.getProperty("user.dir");
			String path = currentDirectory + "/src/main/java/SOFT2412_Assignment2_CC_04_Wed_16_Frank_Gorup/credit_cards.json";

			Object obj = parser
					.parse(new FileReader(path));

			JSONArray jsonArray = (JSONArray) obj;

			ArrayList<Card> cardList = new ArrayList<>();

			for (int i = 0; i < jsonArray.size(); i++) {

				JSONObject jsonObject = (JSONObject) jsonArray.get(i);

				String name = (String) jsonObject.get("name");
				String number = (String) jsonObject.get("number");

				Card card = new Card(name, number);
				cardList.add(card);

			}

			return cardList;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;

	}

}