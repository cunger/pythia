abstract QuestionAnswering = Clauses ** {

  fun

     Get : Predicate -> Imperative_Imp;

     WhatIsThe : Predicate -> Question_QS;
     WhoIsThe  : Predicate -> Question_QS;

}
