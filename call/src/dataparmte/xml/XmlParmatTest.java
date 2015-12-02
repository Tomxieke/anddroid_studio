package dataparmte.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import mylog.Mylog;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.test.AndroidTestCase;
import android.util.Xml;

public class XmlParmatTest extends AndroidTestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	/**
	 * 将xml文件解析成对象
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	public void testParmatXmlFile() throws IOException, XmlPullParserException{
		InputStream in = getContext().getAssets().open("books.xml"); //将文件读入流
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
		
		for(Book b:books){
			Mylog.d(""+b.getName());
		}
	}
	
	/**
	 * 测试定义的工具类接口方法
	 * @throws Exception
	 */
	public void testInterMothe() throws Exception{
		InputStream in = getContext().getAssets().open("books.xml"); //将文件读入流
		XmlParmterUtil util = new XmlParmterUtil();
		ArrayList<Book> books = util.parmateToBook(in);
		
		for(Book b:books){
			Mylog.d(""+b.getName());
		}
	}
	
	
	/**
	 * 将对象转换成xml文件字符串
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws IllegalArgumentException 
	 */
	public void TestSerialize() throws IllegalArgumentException, IllegalStateException, IOException{
		ArrayList<Book> books = new ArrayList<Book>();
		Book  book = new Book();
		book.setId(11);
		book.setName("java宝典");
		book.setPrice("109.0");
		books.add(book);
		
		book = new Book();
		book.setId(111);
		book.setName("java入门");
		book.setPrice("24.0");
		books.add(book);
		
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
		
		Mylog.d(""+writer.toString());
	}
	
	/**
	 * 测试定义的构造xmlstring 工具类方法
	 * @throws Exception
	 */
	public void testInterMothetwo() throws Exception{
		ArrayList<Book> books = new ArrayList<Book>();
		Book  book = new Book();
		book.setId(11);
		book.setName("java宝典");
		book.setPrice("109.0");
		books.add(book);
		
		book = new Book();
		book.setId(111);
		book.setName("java入门");
		book.setPrice("24.0");
		books.add(book);
		
		XmlParmterUtil util = new XmlParmterUtil();
		String xmlStr = util.pramateToXmlStr(books);
		Mylog.d(xmlStr);
	}
	
}
