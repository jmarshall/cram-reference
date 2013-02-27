package uk.ac.ebi.ena.cram.checksum.db;

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
	 * @return
	 */
	public ChecksumSequenceInfo () {
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
}
