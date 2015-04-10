/*
 * CS 157A PROJECT
 * Name: Haoxuan Dong
 * Date: Dec 4 2014
 */
import java.sql.*;
import java.util.*;
public class MyData {
	public static void main(String[] args ){
		String url = "jdbc:mysql://localhost:3306/";
		String user = "root";
		String password = "";
	try {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	}catch(Exception e){
		System.out.println("Not Found");
		System.out.println();
	}//driver loading complete
	try{
		Connection conn = DriverManager.getConnection(url,user,password);
		Statement stt = conn.createStatement();
		stt.execute("create database if not exists test");
		stt.execute("use test");
		stt.execute("set foreign_key_checks = 0");
		createAuthorTable(stt);
		createAuthorISBNTable(stt);
		createPublishersTable(stt);
		//table titles(isbn,title,editionNumber,copyright,publisherID,price)
		createTitlesTable(stt);
		stt.execute("set foreign_key_checks = 1");
		//please execute query after this line
		//Select all authors from the author table, order the
		//table alphabetically by the authors last name and first name
		printLine();
		System.out.println("Select all authors from the author table");
		printLine();
		ResultSet rs = stt.executeQuery("select * from author order by lastName, firstName");
        printHeader(rs);
		while(rs.next()){
			System.out.printf("%-12d %-12s %-12s\n",rs.getInt("authorID"),
		rs.getString("firstName"), rs.getString("lastName"));
		}
		//select all publishers from the publishers table
		printLine();
		System.out.println("Select all publishers from the publishers table");
		printLine();
		rs = stt.executeQuery("select * from publishers");
		printHeader(rs);
		while(rs.next())
			System.out.printf("%-12d %-12s\n",rs.getInt("publisherID"),
					rs.getString("publisherName"));
		//Select a specific publisher and list all books published by 
		//that publisher. Include the title, 
		//year and ISBN number. Order the information
		//alphabetically by title.
		printLine();
		System.out.println("Select a specific publisher and list all books "
				+ "published by that publisher");
		printLine();
		rs = stt.executeQuery("select p.publisherID,p.publisherName,t.title,"
				+ "t.copyright, t.isbn "
				+ "FROM publishers p "
				+ "JOIN titles t "
				+ "ON p.publisherID = t.publisherID "
				+ "AND p.publisherName = 'aaa'"
				+ "ORDER BY title ");
		printHeader(rs);
		while(rs.next())
			System.out.printf("%-12d %-12s %-12s %-12s %-12s\n",
					rs.getInt("publisherID"),rs.getString("publisherName"),
					rs.getString("title"),rs.getString("copyright"),
					rs.getString("isbn"));
		//add new author 
		printLine();
		System.out.println("Add new author");
		printLine();
		System.out.println("Add new author");
		stt.execute("INSERT INTO author(firstName, lastName)"
				+ "VALUES('haoxuan','dong')");
		rs = stt.executeQuery("SELECT * FROM author");
		printHeader(rs);
		while(rs.next())System.out.printf("%-12d %-12s %-12s\n",
				rs.getInt("authorID"), rs.getString("firstName"),
				rs.getString("lastName"));
		//Edit/Update the existing information about an author
		printLine();
		System.out.println("Edit/Update the existing information about an author");
		printLine();
		stt.execute("UPDATE author "
				+ "SET firstName = 'Bilinear', lastName ='Purse' "
				+ "WHERE authorID = 1 ");
		rs = stt.executeQuery("SELECT * FROM author");
		printHeader(rs);
		while(rs.next())System.out.printf("%-12d %-12s %-12s\n",
				rs.getInt("authorID"), rs.getString("firstName"),
				rs.getString("lastName"));
		//Add a new title for an author
		printLine();
		System.out.println("Add a new Title for an author");
		printLine();
		stt.execute("INSERT INTO authorISBN(authorID,isbn) "
				+ "VALUES(16,'1600')");
		stt.execute("INSERT INTO titles(isbn,"
				+ "title, editionNumber, copyright, publisherID, price) "
				+ "VALUES('600', 'Ultimate SQL',1,'2015',5,119.99)");
		rs = stt.executeQuery("SELECT * FROM titles");
		printHeader(rs);
		while(rs.next())System.out.printf("%-12s %-12s %-12d %-12s %-12d %-10.2f\n",
				rs.getString("isbn"),rs.getString("title"),
				rs.getInt("editionNumber"), rs.getString("copyright"),
				rs.getInt("publisherID"), rs.getDouble("price"));
		//add a new publisher
		printLine();
		System.out.println("Add new publisher");
		printLine();
		stt.execute("INSERT INTO publishers(publisherName) "
				+ "VALUES('CS For All')");
		rs = stt.executeQuery("SELECT * FROM publishers");
		printHeader(rs);
		while(rs.next()){
			System.out.printf("%-12d %-12s\n", 
					rs.getInt("publisherID"), rs.getString("publisherName"));
		}
		//Edit/Update the existing information about a publisher
		printLine();
		System.out.println("Edit/Update the existing information about a publisher");
		printLine();
		stt.execute("UPDATE publishers "
				+ "SET publisherName = 'Universal' "
				+ "WHERE publisherID = 1");
		rs = stt.executeQuery("SELECT * FROM publishers");
		printHeader(rs);
		while(rs.next())
			System.out.printf("%-12d %-12s\n", 
					rs.getInt("publisherID"), rs.getString("publisherName"));
		
	
		rs.close();
		stt.close();
		conn.close();
	}catch(Exception e){
		System.out.println("second error");
		e.printStackTrace();
	}
	}
	/*
	 * this helper method prints a line for better formatting
	 */
	private static void printLine(){
		for(int i = 0; i < 60;i++){
			System.out.print("*");
		}
		System.out.println();
	}
	/*
	 * printHeader method prints a formatted header for sql table
	 * Pre:rs an ResultSet from a query
	 * 
	 */
	public static void printHeader(ResultSet rs) throws Exception{
		ResultSetMetaData rsmd = rs.getMetaData();
		int nCols = rsmd.getColumnCount();
		int width = nCols * 12;
		for(int i = 1; i <= nCols; ++i){
			System.out.printf("%-12s ", rsmd.getColumnLabel(i));
		}
		System.out.println();
		for(int i =0;i<width;i++)
			System.out.print("");
		System.out.println();
	}
	/*
	 * create the author table and fill it with 15 entries
	 */
	public static void createAuthorTable(Statement stt) throws Exception{
		stt.execute("drop table if exists author");
	    stt.execute("create table author ("
	    		+ "authorID INT primary key auto_increment,"
	    		+ "firstName char(20) not null,"
	    		+ "lastName char(20) not null"
	    		+ ")");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('a','a')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('b','b')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('c','c')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('d','d')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('e','e')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('f','f')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('g','a')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('h','b')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('i','c')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('j','d')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('k','e')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('l','f')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('m','g')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('n','h')");
	    stt.execute("insert into author(firstName,lastName)"
	    		+ "values('o','i')");

	}
	/*
	 * create the authorISBN table and fill it with 15 entries
	 */
	public static void createAuthorISBNTable(Statement stt) throws Exception{
		stt.execute("drop table if exists authorISBN");
		stt.execute("create table authorISBN ( "+
		"authorID INT not null, isbn char(10) not null,"
		+ "foreign key(authorID) references author(authorID)"
		+ ")");
		stt.execute("insert into authorISBN(authorID, isbn) "
				+ "values(1,'100')");
		stt.execute("insert into authorISBN(authorID, isbn) "
				+ "values(1,'101')");
		stt.execute("insert into authorISBN(authorID, isbn) "
				+ "values(1,'102')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(1,'103')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(1,'104')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(2,'200')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(2,'201')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(2,'202')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(2,'203')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(3,'300')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(3,'301')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(3,'302')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(4,'400')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(4,'401')");
		stt.execute("insert into  authorISBN(authorID, isbn) "
				+ "values(5,'500')");


	}
	/*
	 * create the createPublishers table and fill it with 15 entries
	 */
	public static void createPublishersTable(Statement stt)throws Exception{
		stt.execute("drop table if exists publishers");
		stt.execute("create table publishers(publisherID INT primary key auto_increment,"
				+ "publisherName char(100) not null"
				+ ")");
		stt.execute("insert into publishers(publisherName) "
				+ "values('aaa')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('bbb')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('ccc')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('ddd')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('eee')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('fff')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('ggg')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('hhh')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('iii')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('jjj')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('kkk')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('lll')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('mmm')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('nnn')");
		stt.execute("insert into publishers(publisherName) "
				+ "values('ooo')");

		


	}
	/*
	 * create the titles table and fill it with 15 entries
	 */
	public static void createTitlesTable(Statement stt) throws Exception{
		stt.execute("drop table if exists titles");
		stt.execute("create table titles(isbn char(10) primary key,"
				+ "title varchar(500) not null,"
				+ "editionNumber INT not null,"
				+ "copyright char(4) not null,"
				+ "publisherID INT not null,"
				+ "price decimal(8,2) not null,"
				+ "foreign key(publisherID) references publishers(publisherID) ON UPDATE CASCADE"
				+ ")");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('100','a 1st book',1, '2014',1,29.99)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('101','a 2nd book',1, '2013',2,29.98)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('102','a 3rd book',1, '2012',3,29.97)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('103','a 4th book',1, '2011',4,29.96)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('104','a 5th book',2, '2010',5,29.95)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('200','b 1st book',2, '2009',4,29.94)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('201','b 2nd book',1, '2008',3,29.93)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('202','b 3rd book',1, '2007',2,29.92)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('203','b 4th book',1, '2008',1,29.91)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('300','c 1st book',1, '2009',1,29.90)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('301','c 2nd book',1, '2010',2,29.19)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('302','c 3rd book',1, '2011',3,29.29)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('400','d 1st book',1, '2012',2,29.39)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('401','d 2nd book',1, '2013',1,29.49)");
		stt.execute("insert into titles(isbn,title,editionNumber,copyright,publisherID,price) "
				+ "values('500','e 1st book',1, '2014',1,29.59)");


	}
	
}
