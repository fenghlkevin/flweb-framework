package cn.com.cennavi.kfgis.util.csv;

import java.io.IOException;
import java.util.List;

public interface CSVReader {
	
	/** The default separator to use if none is supplied to the constructor. */
    public static final char DEFAULT_SEPARATOR = ',';

    /**
     * The default quote character to use if none is supplied to the
     * constructor.
     */
    public static final char DEFAULT_QUOTE_CHARACTER = '"';
    
    /**
     * The default line to start reading.
     */
    public static final int DEFAULT_SKIP_LINES = 0;
	
	public List<String[]> readAll() throws IOException;

	public String[] readNext() throws IOException;
	
	public void close() throws IOException;
}
