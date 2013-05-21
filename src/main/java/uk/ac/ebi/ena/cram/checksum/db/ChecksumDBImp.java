package uk.ac.ebi.ena.cram.checksum.db;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

public class ChecksumDBImp {
	public static final String SELECT_CHECKSUM_DB_INFO_BY_MD5 = "uk.ac.ebi.ena.cram.checksum.db.selectChecksumSequenceInfoByMD5";
	public static final String SELECT_CHECKSUM_DB_INFO_BY_SHA1 = "uk.ac.ebi.ena.cram.checksum.db.selectChecksumSequenceInfoBySHA1";
	public static final String INSERT_CHECKSUM_DB_INFO = "uk.ac.ebi.ena.cram.checksum.db.insertChecksumSequenceInfo";
	public static final String TEST_DATABASE = "uk.ac.ebi.ena.cram.checksum.db.testDatabase";
	
//	private static SqlSessionFactory sqlSessionFactory;
	static Logger log = Logger.getLogger(ChecksumDBImp.class);
	private SqlSession session;
	public static final String TOMCAT_ETAPRO_ENV = "ETAPRO_TOMCAT";
	
	/*
	static {
		String resource = "uk/ac/ebi/ena/cram/checksum/db/Configuration.xml";
		try {
			Reader reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "ETATST_UNPOOLED");
		} catch (IOException e) {
			log.error("Error creating sqlSessionFactory", e);
		}
	}
	*/
	
	public ChecksumDBImp (String environment) throws Exception {
		String resource = "uk/ac/ebi/ena/cram/checksum/db/Configuration.xml";
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, environment);
			session = sqlSessionFactory.openSession();
			log.info("connected to " + session);
		} catch (Exception e) {
			log.error("Error ChecksumDBImp",e);
			throw e;
		}
	}
	
	public ChecksumDBImp () throws Exception {
		String resource = "uk/ac/ebi/ena/cram/checksum/db/Configuration.xml";
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(resource);
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			session = sqlSessionFactory.openSession();
		} catch (Exception e) {
			log.error("Error ChecksumDBImp",e);
			throw e;
		}
	}
	
	public String toString () {
		try {
			return session.getConnection().getMetaData().getURL().toString();
		} catch (SQLException e) {
			log.info(e);
			return e.getMessage();
		}
		
	}
	
	/**
	 * 
	 * @param md5Checksum
	 * @return
	 * @throws Exception
	 */
	public ChecksumSequenceInfo selectChecksumSequenceInfoByMD5 (String md5Checksum) throws Exception {
		ChecksumSequenceInfo checksumSequenceInfo = null;
		try {
			checksumSequenceInfo = (ChecksumSequenceInfo) session.selectOne(SELECT_CHECKSUM_DB_INFO_BY_MD5, md5Checksum);
		} catch (Exception e) {
			log.error("selectChecksumSequenceInfoByMD5" + e.getMessage());
			throw new Exception(e.getMessage());
		} 
		return checksumSequenceInfo;
	}
	
	/**
	 * 
	 * @param sha1Checksum
	 * @return
	 * @throws Exception
	 */
	public ChecksumSequenceInfo selectChecksumSequenceInfoBySHA1 (String sha1Checksum) throws Exception {
		ChecksumSequenceInfo checksumSequenceInfo = null;
		try {
			checksumSequenceInfo = (ChecksumSequenceInfo) session.selectOne(SELECT_CHECKSUM_DB_INFO_BY_SHA1, sha1Checksum);
		} catch (Exception e) {
			log.error("selectChecksumSequenceInfoByMD5" + e.getMessage());
			throw new Exception(e.getMessage());
		} 
		return checksumSequenceInfo;
	}
	
	/**
	 * 
	 * @param checksumSequenceInfo
	 * @return
	 * @throws Exception
	 */
	public int insertChecksumSequenceInfo(ChecksumSequenceInfo checksumSequenceInfo) throws Exception {
		int rowsInserted = 0;
		try {
			rowsInserted = session.insert(INSERT_CHECKSUM_DB_INFO, checksumSequenceInfo);
		} catch (Exception e) {
			log.error("insertChecksumSequenceInfo" + e.getMessage());
			throw new Exception(e.getMessage());
		} 
		return rowsInserted;
	}
	
	public String testDatabase () throws Exception {
	    String dateString = null;
        try {
            dateString = (String) session.selectOne(TEST_DATABASE);
        } catch (Exception e) {
            log.error("selectChecksumSequenceInfoByMD5" + e.getMessage());
            throw new Exception(e.getMessage());
        } 
        return dateString;
	}
	
	public void closeSession () {
		log.info("Disconnected from " + session);
		session.close();
	}
	
}