
abstract Core_ = {

 cat
  
    Class;

    Entity;
   [Entity] {2};

    Predicate;
    Relation;

    Statement;


 fun 

    owl_Thing : Class;

    class2predicate  : Class -> Predicate;
    entity2predicate : Entity -> Predicate;


    ---- Coordination ----

    AndEntity, OrEntity : [Entity] -> Entity; 


    ---- Determiners ----

    Some1, All1, No1 : Predicate -> Predicate -> Statement; -- quantifiers in subject position
    Some2, All2, No2 : Predicate -> Relation  -> Predicate; -- quantifiers in object  position

    The_empty       : Entity -> Entity; 
    The, This, That : Predicate -> Entity; 
 -- These, Those    : Predicate -> [Entity]; 

    Only : Entity -> Entity;


    ---- Application ----

    apply1 : Predicate -> Entity -> Statement; 
    apply2 : Relation  -> Entity -> Entity -> Statement;

    partial_apply1 : Relation -> Entity -> Predicate; -- supplying subject
    partial_apply2 : Relation -> Entity -> Predicate; -- supplying object

    existential_closure : Relation -> Predicate;


    ---- Modification ---- 

    modify : Predicate -> Predicate -> Predicate; -- first one is the modifier, second one the modified


    ---- Placeholder expressions -----

    -- For NER -- 

    Entity1, Entity2, Entity3, Entity4, Entity5, Entity6, Entity7, Entity8, Entity9 : Entity;

    -- For robust parsing -- 

    UnknownClass     : Class;
    UnknownEntity    : Entity;
    UnknownPredicate : Predicate;
    UnknownRelation  : Relation;

    ---- Semantically light expressions ----

    have_Rel    : Relation;
    with_Rel    : Relation;
    possess_Rel : Relation;
    in_Rel      : Relation;
    from_Rel    : Relation;

}
