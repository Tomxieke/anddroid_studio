package dataparmte.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class XmlParmterUtil implements XmlparmateInter {

	@Override
	public ArrayList<Book> parmateToBook(InputStream in) throws Exception {
		XmlPullParser pullParser = Xml.newPullParser();  //得到xml解析器
		pullParser.setInput(in, "UTF-8");
		int type = pullParser.getEventType();  //取到第一个位置的标签
		ArrayList<Book> books = null;
		Book book = null;
		while(type != XmlPullParser.END_DOCUMENT){   //不是结束文档标签
			switch(type){
			case XmlPullParser.START_DOCUMENT: //是开始文档标签  <books>
				books = new ArrayList<Book>();
				break;
			case XmlPullParser.START_TAG:  //是开始标签  <book>
				if(pullParser.getName().equals("book")){
					book = new Book();
				}else if(pullParser.getName().equals("id")){
					type = pullParser.next();  //移到下一个标签
					int id = Integer.parseInt(pullParser.getText());
					book.setId(id);
				}else if(pullParser.getName().equals("name")){
					type = pullParser.next();
					String name = pullParser.getText();
					book.setName(name);
				}else if(pullParser.getName().equals("price")){
					type = pullParser.next();
					String price = pullParser.getText();
					book.setPrice(price);
				}
				break;
			case XmlPullParser.END_TAG:  //是结束标签  如</book>
				if(pullParser.getName().equals("book")){
					books.add(book);
					book = null;  //释放内存
				}
				break;
			}
			type = pullParser.next();
		}
		return books;
	}

	@Override
	public String pramateToXmlStr(ArrayList<Book> books) throws IllegalArgumentException, IllegalStateException, IOException {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);
		
		serializer.startDocument("UTF-8", true);  //文档开始标签
		serializer.startTag("", "books");  //开始标签
		
		for(Book b:books){
			serializer.startTag("", "book");
			
			serializer.startTag("", "id");
			serializer.text(String.valueOf(b.getId()));
			serializer.endTag("", "id");
			
			serializer.startTag("", "name");
			serializer.text(b.getName());
			serializer.endTag("", "name");
			
			serializer.startTag("", "price");
			serializer.text(b.getPrice());
			serializer.endTag("", "price");
			
			serializer.endTag("", "book");
		}
		serializer.endTag("", "books");
		serializer.endDocument();
		return writer.toString();
	}

}
