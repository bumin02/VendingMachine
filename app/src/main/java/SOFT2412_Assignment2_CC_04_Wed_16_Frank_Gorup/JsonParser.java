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

	ArrayList<Items> itemList = new ArrayList<>();

	/**
	 * This method reads the configuration file and creates the pool table
	 * @param path
	 */
	public void parse(String path) {

		JSONParser parser = new JSONParser();
		try {
			Object object = parser.parse(new FileReader(path));

			JSONObject jsonObject = (JSONObject) object;

			JSONArray jsonDrinks = (JSONArray) jsonObject.get("Drinks");

			for (Object obj : jsonDrinks) {
				JSONObject item = (JSONObject) obj;

				String name = (String) item.get("name");
				String code = (String) item.get("code");
				int qty = (int) item.get("qty");
				Long price = (Long) item.get("price");

				// System.out.println("Name: " + name + " Code: " + code + " qty: " + qty + " price: " + price);
				Items item1 = new Items(name, code, "Drink", price, qty);
				itemList.add(item1);
			}

			JSONArray jsonChocolates = (JSONArray) jsonObject.get("Chocolates");

			for (Object obj : jsonChocolates) {
				JSONObject item = (JSONObject) obj;

				String name = (String) item.get("name");
				String code = (String) item.get("code");
				int qty = (int) item.get("qty");
				Long price = (Long) item.get("price");

				// System.out.println("Name: " + name + " Code: " + code + " qty: " + qty + " price: " + price);
				Items item1 = new Items(name, code, "Chocolate", price, qty);
				itemList.add(item1);
			}

			JSONArray jsonChips = (JSONArray) jsonObject.get("Chips");

			for (Object obj : jsonChips) {
				JSONObject item = (JSONObject) obj;

				String name = (String) item.get("name");
				String code = (String) item.get("code");
				int qty = (int) item.get("qty");
				Long price = (Long) item.get("price");

				// System.out.println("Name: " + name + " Code: " + code + " qty: " + qty + " price: " + price);
				Items item1 = new Items(name, code, "Chip", price, qty);
				itemList.add(item1);
			}

			JSONArray jsonCandies = (JSONArray) jsonObject.get("Candies");

			for (Object obj : jsonCandies) {
				JSONObject item = (JSONObject) obj;

				String name = (String) item.get("name");
				String code = (String) item.get("code");
				int qty = (int) item.get("qty");
				Long price = (Long) item.get("price");

				// System.out.println("Name: " + name + " Code: " + code + " qty: " + qty + " price: " + price);
				Items item1 = new Items(name, code, "Candy", price, qty);
				itemList.add(item1);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Items> getItemList(){
		return itemList;
	}
}