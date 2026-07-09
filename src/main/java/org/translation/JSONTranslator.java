package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private static final String ALPHA3 = "alpha3";
    private List<String> countries;
    private Map<String, ArrayList<String>> countryWithLanguageCode;
    private Map<String, Map<String, String>> countryWithLanguage;

    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */
    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            this.countries = new ArrayList<>();
            this.countryWithLanguageCode = new HashMap<>();
            this.countryWithLanguage = new HashMap<>();
            String country;
            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                ArrayList<String> codes = new ArrayList<>();
                Map<String, String> map = new HashMap<>();
                JSONObject obj = jsonArray.getJSONObject(i);
                country = obj.getString(ALPHA3);
                this.countries.add(country);
                for (String key: obj.keySet()) {
                    if (!("id".equals(key) || "alpha2".equals(key) || "alpha3".equals(key))) {
                        codes.add(key);
                        map.put(key, obj.getString(key));
                    }
                }
                this.countryWithLanguageCode.put(country, codes);
                this.countryWithLanguage.put(country, map);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        List<String> languages = this.countryWithLanguageCode.get(country);
        if (languages == null) {
            return null;
        }
        return new ArrayList<>(languages);
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(this.countries);
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        Map<String, String> codeWithLanguage = this.countryWithLanguage.get(country);
        if (codeWithLanguage == null) {
            return null;
        }
        return codeWithLanguage.get(language);
    }
}
