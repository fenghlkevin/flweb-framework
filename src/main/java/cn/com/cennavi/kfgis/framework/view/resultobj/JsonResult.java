package cn.com.cennavi.kfgis.framework.view.resultobj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import cn.com.cennavi.kfgis.framework.util.ObjUtil;

public class JsonResult extends AbstractResult {

	private static ObjectMapper objectMapper = null;
	static {
		objectMapper = new ObjectMapper();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1608165118646766726L;

	private Object jsonObj = null;
	
	private String callback;

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public void setJsonObj(Object jsonObj) {
		this.jsonObj = jsonObj;
	}

	public byte[] getJsonObj() throws IOException {
		if (jsonObj == null) {
			return new byte[0];
		}
		
		ByteArrayOutputStream byteo = null;
		byte[] res=null;
		try {
			byteo = new ByteArrayOutputStream();
			JsonGenerator generator = objectMapper.getJsonFactory().createJsonGenerator(byteo, JsonEncoding.UTF8);
			objectMapper.writeValue(generator, jsonObj);
			byte[] bs = byteo.toByteArray();
			if(!ObjUtil.isEmpty(callback)){
				byte[] st=(callback+"(").getBytes();
				byte[] ed=")".getBytes();
				byte[] temp=new byte[st.length+bs.length+ed.length];
				System.arraycopy(st, 0, temp, 0, st.length);
				System.arraycopy(bs, 0, temp, st.length, bs.length);
				System.arraycopy(ed, 0, temp, st.length+bs.length, ed.length);
				res=temp;
			}else{
				res=bs;
			}
			
			return res;
		} finally {
			if (byteo != null) {
				byteo.close();
			}
		}
	}
}
