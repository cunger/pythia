
abstract PredictiveAnalytics = Clauses ** {

 cat 

    Prediction;
    Attitude;

 fun

    predict     : Statement -> Prediction; 
    predict_not : Statement -> Prediction;
    
    probability_exact : Prediction -> Entity xsd_float -> Sentence_S;
    probability_descr : Prediction -> Measure          -> Sentence_S;
    confidence        : Prediction -> Attitude         -> Sentence_S;

    Think, Expect : Attitude;

    plain : Prediction -> Sentence_S;

}
