package msi.gama.util.file;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import msi.gama.interfaces.*;
import msi.gama.internal.types.*;
import msi.gama.kernel.exceptions.GamaRuntimeException;
import msi.gama.util.GamaPoint;
import msi.gama.util.matrix.GamaIntMatrix;
import msi.gaml.operators.Files;

public class GamaImageFile extends GamaFile<GamaPoint, Integer> {

	public GamaImageFile(final IScope scope, final String pathName) throws GamaRuntimeException {
		super(scope, pathName);
	}

	public GamaImageFile(final String absoluteFilePath) throws GamaRuntimeException {
		super(absoluteFilePath);
	}

	@Override
	protected void fillBuffer() throws GamaRuntimeException {
		if ( buffer != null ) { return; }
		buffer = isPgmFile() ? matrixValueFromPgm(null) : matrixValueFromImage(null);
	}

	@Override
	protected void checkValidity() throws GamaRuntimeException {
		super.checkValidity();
		if ( !GamaFileType.isImageFile(file.getName()) ) { throw new GamaRuntimeException(
			"The extension " + this.getExtension() + " is not recognized for image files"); }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see msi.gama.util.GamaFile#flushBuffer()
	 */
	@Override
	protected void flushBuffer() throws GamaRuntimeException {
		// TODO Create a rendered image from the Matrix.
		// Use ImageIO to write it.
	}

	@Override
	protected IGamaFile _copy() {
		return null;
	}

	@Override
	protected boolean _isFixedLength() {
		return true;
	}

	@Override
	protected IMatrix _matrixValue(final IScope scope, final GamaPoint preferredSize)
		throws GamaRuntimeException {
		if ( preferredSize != null ) { return matrixValueFromImage(preferredSize); }
		return (IMatrix) buffer;
	}

	@Override
	protected String _stringValue() throws GamaRuntimeException {
		fillBuffer();
		return buffer.stringValue();
	}

	private IMatrix matrixValueFromImage(final GamaPoint preferredSize) throws GamaRuntimeException {
		BufferedImage colorImage = null;
		try {
			colorImage = ImageIO.read(file);
		} catch (final Exception e) {
			throw new GamaRuntimeException(e);
		}
		int xSize, ySize;
		if ( preferredSize == null ) {
			xSize = colorImage.getWidth();
			ySize = colorImage.getHeight();
		} else {
			xSize = (int) preferredSize.x;
			ySize = (int) preferredSize.y;
			final BufferedImage resultingImage =
				new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_RGB);
			final Graphics2D g = resultingImage.createGraphics();
			g.drawImage(colorImage, 0, 0, xSize, ySize, null);
			g.dispose();
			colorImage = resultingImage;
		}
		final IMatrix matrix = new GamaIntMatrix(xSize, ySize);
		for ( int i = 0; i < xSize; i++ ) {
			for ( int j = 0; j < ySize; j++ ) {
				matrix.put(i, j, colorImage.getRGB(i, j));
			}
		}
		return matrix;
	}

	private IMatrix matrixValueFromPgm(final GamaPoint preferredSize) throws GamaRuntimeException {
		// TODO PreferredSize is not respected here
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			StringTokenizer tok;
			String str = in.readLine();
			if ( !str.equals("P2") ) { throw new UnsupportedEncodingException(
				"File is not in PGM ascii format"); }
			str = in.readLine();
			if ( str == null ) { return GamaMatrixType.with(0, preferredSize); }
			tok = new StringTokenizer(str);
			final int xSize = Integer.valueOf(tok.nextToken());
			final int ySize = Integer.valueOf(tok.nextToken());
			in.readLine();
			StringBuffer buf = new StringBuffer();
			String line = in.readLine();
			while (line != null) {
				buf.append(line);
				buf.append(' ');
				line = in.readLine();
			}
			in.close();
			str = buf.toString();
			tok = new StringTokenizer(str);
			final IMatrix matrix = new GamaIntMatrix(xSize, ySize);
			for ( int i = 0; i < xSize; i++ ) {
				for ( int j = 0; j < ySize; j++ ) {
					matrix.put(j, i, Integer.valueOf(tok.nextToken()));
				}
			}
			return matrix;
		} catch (final Exception ex) {
			throw new GamaRuntimeException(ex);
		} finally {
			if ( in != null ) {
				try {
					in.close();
				} catch (IOException e) {
					throw new GamaRuntimeException(e);
				}
			}
		}
	}

	@Override
	public String getKeyword() {
		return Files.IMAGE;
	}

}
