package models.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import models.classes.Material;
import models.classes.Material.PricePolicy;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.ImageUtils;
import utils.ZeroPagesException;

public class MaterialUtil {
    
    public static Material generateFromForm(MultipartFormData body)
	    throws FileNotFoundException, ZeroPagesException,
	    IllegalArgumentException {

	FilePart materialFile = body.getFile("materialFile");
	Map<String, String[]> formContent = body.asFormUrlEncoded();
	String[] price = formContent.get("price");
	String[] title = formContent.get("title");
	String[] description = formContent.get("description");
	String[] pricePolicy = formContent.get("pricePolicy");

	PricePolicy policy = PricePolicy.valueOf(pricePolicy[0]);

	Material material = new Material();
	material.setTitle(title[0]);
	material.setPricePolicy(policy);
	material.setPrice(price[0]);
	material.setDescription(description[0]);

	byte[] thumbnail = null;
	byte[] lob = null;
	if (materialFile != null) {
	    try {
		lob = IOUtils.toByteArray(new FileInputStream(materialFile.getFile()));
		thumbnail = ImageUtils.generateThumbnail(
			materialFile.getContentType(), lob);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}

	material.setMaterialFile(lob);
	material.setMaterialThumbnail(thumbnail);
	material.setModifiedAt(new Date());

	return material;
    }
}