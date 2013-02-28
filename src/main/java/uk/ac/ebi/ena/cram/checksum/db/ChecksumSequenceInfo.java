package uk.ac.ebi.ena.cram.checksum.db;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumSequenceInfo {
	private String md5Checksum;
	private String sha1Checksum;
	private String sequence;

	/**
	 * @param md5Checksum
	 * @param sha1Checksum
	 * @param sequence
	 */
	public ChecksumSequenceInfo(String md5Checksum, String sha1Checksum,
			String sequence) {
		super();
		this.md5Checksum = md5Checksum;
		this.sha1Checksum = sha1Checksum;
		this.sequence = sequence;
	}

	/**
	 * Default empty constructor
	 * 
	 * @return
	 */
	public ChecksumSequenceInfo() {
		super();
	}

	public String getMd5Checksum() {
		return md5Checksum;
	}

	public void setMd5Checksum(String md5Checksum) {
		this.md5Checksum = md5Checksum;
	}

	public String getSha1Checksum() {
		return sha1Checksum;
	}

	public void setSha1Checksum(String sha1Checksum) {
		this.sha1Checksum = sha1Checksum;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public static char[] computeBase64Checksum(String sequence,
			MessageDigest algorithm) throws NoSuchAlgorithmException {
		// MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(sequence.toUpperCase().getBytes());
		return javax.xml.bind.DatatypeConverter
				.printBase64Binary(algorithm.digest()).substring(0, 22)
				.toCharArray();
	}

	public static ChecksumSequenceInfo createChecksumSequenceInfo(
			String sequence) throws NoSuchAlgorithmException {
		ChecksumSequenceInfo checksumSequenceInfo = new ChecksumSequenceInfo();
		String md5Checksum = new String(computeBase64Checksum(sequence,
				MessageDigest.getInstance("MD5")));
		String sha1Checksum = new String(computeBase64Checksum(sequence,
				MessageDigest.getInstance("SHA1")));
		checksumSequenceInfo.setSequence(sequence);
		checksumSequenceInfo.setMd5Checksum(md5Checksum);
		checksumSequenceInfo.setSha1Checksum(sha1Checksum);
		return checksumSequenceInfo;
	}
}
