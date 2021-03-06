package uk.ac.ebi.ena.cram.rest;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uk.ac.ebi.ena.cram.checksum.db.ChecksumDBImp;
import uk.ac.ebi.ena.cram.checksum.db.ChecksumSequenceInfo;

@Path("/")
public class ChecksumService {

    @GET
    @Path("/test")
    public Response testDatabase() {
        ChecksumDBImp checksumDBImp = null;
        String dateString = null;
        try {
            checksumDBImp = new ChecksumDBImp(ChecksumDBImp.TOMCAT_ETAPRO_ENV);
            dateString = checksumDBImp.testDatabase();
            checksumDBImp.closeSession();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }

        if (dateString != null) {
            return Response.ok().entity(dateString).build();
        }
        else {
            return Response.status(404).build();
        }
 
 
    }
    
	
		@GET
		@Path("/md5/{param}")
		public Response getSequenceForMD5Checksum(@PathParam("param") String msg) {
			ChecksumDBImp checksumDBImp = null;
			ChecksumSequenceInfo checksumSequenceInfo = null;
			try {
				checksumDBImp = new ChecksumDBImp(ChecksumDBImp.TOMCAT_ETAPRO_ENV);
				checksumSequenceInfo = checksumDBImp.selectChecksumSequenceInfoByMD5(msg);
				checksumDBImp.closeSession();
			} catch (Exception e) {
//				e.printStackTrace();
				return Response.status(404).entity(e.getMessage()).build();
			}

			if (checksumSequenceInfo != null) {
				return Response.ok().entity(checksumSequenceInfo.getSequence()).build();
			}
			else {
				return Response.status(404).build();
			}
	 
	 
		}
		
		@GET
		@Path("/sha1/{param}")
		public Response getSequenceForSHA1Checksum(@PathParam("param") String msg) {
			ChecksumDBImp checksumDBImp = null;
			ChecksumSequenceInfo checksumSequenceInfo = null;
			try {
				checksumDBImp = new ChecksumDBImp(ChecksumDBImp.TOMCAT_ETAPRO_ENV);
				checksumSequenceInfo = checksumDBImp.selectChecksumSequenceInfoBySHA1(msg);
				checksumDBImp.closeSession();
			} catch (Exception e) {
//				e.printStackTrace();
				return Response.status(200).entity(e.getMessage()).build();
			}

			if (checksumSequenceInfo != null) {
				return Response.status(200).entity(checksumSequenceInfo.getSequence()).build();
			}
			else {
				return Response.noContent().build();
			}
	 
		}
		
		/*
		@Path("/helloworld")
		public class HelloWorldResource {

				@GET
				@Produces("text/plain")
				public String getClichedMessage() {
					return "Hello World";
			}
		}
		*/
}
