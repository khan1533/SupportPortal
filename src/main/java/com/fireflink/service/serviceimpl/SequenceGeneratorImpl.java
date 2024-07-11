package com.fireflink.service.serviceimpl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fireflink.model.DatabaseSequence;
import com.fireflink.service.SequenceGeneratorService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class SequenceGeneratorImpl implements SequenceGeneratorService{

//	private final MongoTemplate mongoTemplate;
	private final MongoOperations mongoOperations;
	
	
	public String generetedSequence(String seqName) {
		
		
//		mongoOperations.findAndModify(Query., new Update().inc("seq", 1), Options().)
		
		
		
		Query query = new Query(Criteria.where("_id").is(seqName));
		DatabaseSequence counter = mongoOperations.findOne(query, DatabaseSequence.class);
		if(counter==null) {
			counter  =new DatabaseSequence();
			counter.setId(seqName);
			counter.setSeq(String.valueOf(1000));
			mongoOperations.save(counter);
			
		}
		
		int seq=Integer.parseInt( counter.getSeq())+1;
		counter.setSeq(String.valueOf(seq));
		mongoOperations.save(counter);
		return "FF-"+String.valueOf(seq);
		
		
		
		
//		Query query=new Query(Criteria.where("_id").is(seqName));
//		DatabaseSequence sequence = mongoTemplate.findOne(query, DatabaseSequence.class);
//		if(sequence==null) {
//			
//			sequence=new DatabaseSequence();
//			sequence.set_id(seqName);
//			sequence.setSequence(1000);
//			mongoTemplate.save(sequence);
//		}
//		
//		DatabaseSequence counter = mongoOperations.findAndModify ( 
//				query,
//				new Update().inc("seqName", 1),
//				FindAndModifyOptions.options().returnNew(true).upsert(true),
//				DatabaseSequence.class );
//		
//		return !Objects.isNull(counter) ? counter.getSequence() : 1;
		
		
	}
}
