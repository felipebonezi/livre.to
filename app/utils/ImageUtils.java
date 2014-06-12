package utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class ImageUtils {

    public static byte[] generateThumbnail(String mimetype, byte[] data)
	    throws ZeroPagesException, IOException {

	if (mimetype.equals("application/pdf")) {
	    return generateThumbnailFromPdf(data);
	}

	return null;
    }

    public static byte[] generateThumbnailFromPdf(byte[] pdf)
	    throws ZeroPagesException, IOException {

	PDDocument document = PDDocument.load(new ByteArrayInputStream(pdf));
	List<?> pages = document.getDocumentCatalog().getAllPages();
	if (pages.size() == 0) {
	    throw new ZeroPagesException();
	}

	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ImageIO.write(((PDPage) pages.get(0)).convertToImage(), "png", baos);
	byte[] res = baos.toByteArray();

	return res;

    }
}
