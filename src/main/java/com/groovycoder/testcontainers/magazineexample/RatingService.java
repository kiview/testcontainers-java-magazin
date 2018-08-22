package com.groovycoder.testcontainers.magazineexample;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class RatingService {

    private final TalkRepository talkRepository;
    private final RatingRepository ratingRepository;

    public RatingService(TalkRepository talkRepository, RatingRepository ratingRepository) {
        this.talkRepository = talkRepository;
        this.ratingRepository = ratingRepository;
    }

    public void recordRating(Rating rating) throws SQLException, ExecutionException, InterruptedException {

        if (talkRepository.get(rating.getTalkId()).isPresent()) {
            ratingRepository.add(rating);
        }

    }

}
