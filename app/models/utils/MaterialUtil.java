package models.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import models.classes.Material;
import models.classes.Material.PricePolicy;

import org.apache.commons.io.IOUtils;

import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import utils.FormatNotSupportedException;
import utils.ImageUtils;
import utils.ZeroPagesException;

public class MaterialUtil {

	public static final List<String> SUPPORTED_MIMETYPES = Arrays.asList("application/pdf");
	private static final String ERR_MIMETYPE_NOT_SUPPORTED = "O formato %s não é suportado.";
	
	public static Material generateFromForm(MultipartFormData body) throws FileNotFoundException, 
		ZeroPagesException, IllegalArgumentException, FormatNotSupportedException {

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
			if(!SUPPORTED_MIMETYPES.contains(materialFile.getContentType())) {
				throw new FormatNotSupportedException(String.format(ERR_MIMETYPE_NOT_SUPPORTED, materialFile.getContentType()));
			}

			try {
				lob = IOUtils.toByteArray(new FileInputStream(materialFile.getFile()));
				thumbnail = ImageUtils.generateThumbnail(materialFile.getContentType(), lob);
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
