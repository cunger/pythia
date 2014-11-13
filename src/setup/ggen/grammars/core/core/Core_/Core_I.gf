--# -path=.:prelude:alltenses

incomplete concrete Core_I of Core_ = open Basic, Syntax, Extra in {


 lincat

    Class     = PredicateRecord;

    Entity    = EntityRecord;
   [Entity]   = [NP];

    Predicate = PredicateRecord;
    Relation  = RelationRecord;

    Statement = Cl;
 

 lin   

    class2predicate  c = c;
    entity2predicate e = mkPredicate e.np;


    ---- Coordination ----

    BaseEntity e1 e2 = mkListNP e1.np e2.np; 
    ConsEntity e1 e2 = mkListNP e1.np e2;  
    AndEntity  e     = mkEntity (mkNP and_Conj e); 
    OrEntity   e     = mkEntity (mkNP or_Conj  e);


    ---- Determiners ----

    Some1 p1 p2 = basic_apply p2 (mkEntity (mkNP aSg_Det p1.cn));
    No1   p1 p2 = basic_apply p2 (mkEntity (mkNP (mkDet no_Quant) p1.cn));
    All1  p1 p2 = basic_apply p2 (mkEntity (variants { mkNP all_Predet (mkNP aPl_Det p1.cn); 
                                                         mkNP every_Det p1.cn }));
    Generic1 p1 p2 = basic_apply p2 (mkEntity (mkNP aPl_Det p1.cn));

    Some2 p r = basic_apply r (mkEntity (mkNP aSg_Det p.cn));
    No2   p r = basic_apply r (mkEntity (mkNP (mkDet no_Quant) p.cn));
    All2  p r = basic_apply r (mkEntity (variants { mkNP all_Predet (mkNP aPl_Det p.cn); 
                                                        mkNP every_Det p.cn }));
    Generic2 p r = basic_apply r (mkEntity (mkNP aPl_Det p.cn));

    The  c = mkEntity (mkNP the_Det c.cn);
    This c = mkEntity (mkNP this_Det c.cn);
    That c = mkEntity (mkNP that_Det c.cn);

    Only e = mkEntity (mkNP only_Predet e.np);


    ---- Application ----

    apply1 p e     = basic_apply p e; 
    apply2 r e1 e2 = basic_apply e1 r e2;

    partial_apply1 r e = basic_apply r e;
    partial_apply2 r e = basic_apply e r;

    existential_closure r = basic_closure r;


    ---- Modification ---- 

    modify p1 p2 = mkPredicate (variants { mkCN p1.ap p2.cn; 
                                           mkCN p1.cn p2.adv; 
                                           mkCN p1.cn (mkRS (mkRCl which_RP (makeVP p2))) })
                               (mkVP p1.vp p2.adv)
                               (mkAP    and_Conj p1.ap  p2.ap)
                               (makeAdv and_Conj p1.adv p2.adv);


    ---- Placeholder expressions ---- 

    -- For NER --

    Entity1 = mkEntity (mkPN "Entity1");
    Entity2 = mkEntity (mkPN "Entity2");
    Entity3 = mkEntity (mkPN "Entity3");
    Entity4 = mkEntity (mkPN "Entity4");
    Entity5 = mkEntity (mkPN "Entity5");
    Entity6 = mkEntity (mkPN "Entity6");
    Entity7 = mkEntity (mkPN "Entity7");
    Entity8 = mkEntity (mkPN "Entity8");
    Entity9 = mkEntity (mkPN "Entity9");

    -- For robust parsing -- 

    UnknownClass     = mkPredicate DUMMY_CN DUMMY_VP DUMMY_AP DUMMY_Adv;
    UnknownEntity    = mkEntity    DUMMY_NP;
    UnknownPredicate = mkPredicate DUMMY_CN DUMMY_VP DUMMY_AP DUMMY_Adv;
    UnknownRelation  = mkRelation  DUMMY_N2 DUMMY_V2 DUMMY_A2 DUMMY_Prep;

    ---- Semantically light expressions ----

    have_Rel    = mkRelation have_V2;
    with_Rel    = mkRelation with_Prep;
    possess_Rel = mkRelation possess_Prep;
    in_Rel      = mkRelation in_Prep;
    from_Rel    = mkRelation from_Prep;

}
