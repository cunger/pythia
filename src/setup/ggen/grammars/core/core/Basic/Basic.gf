--# -path=.:prelude:alltenses

interface Basic = open Prelude, Syntax in {

  oper 

    -- Records

    EntityRecord    = { np : NP };
    PredicateRecord = { cn : CN; vp : VP; ap : AP; adv  : Adv }; -- vpSlash
    RelationRecord  = { n2 : N2; v2 : V2; a2 : A2; prep : Prep; reverse : Bool }; -- vs : VS

    -- Construction methods

    mkPredicate = overload {
    mkPredicate : CN  -> PredicateRecord = \ cn  -> { cn = cn;   vp = mkVP cn;  ap = NONE; adv = NONE };
    mkPredicate : VP  -> PredicateRecord = \ vp  -> { cn = NONE; vp = vp;       ap = NONE; adv = NONE };
    mkPredicate : AP  -> PredicateRecord = \ ap  -> { cn = NONE; vp = mkVP ap;  ap = ap;   adv = NONE };
    mkPredicate : Adv -> PredicateRecord = \ adv -> { cn = NONE; vp = mkVP adv; ap = NONE; adv = adv  };
    mkPredicate : NP  -> PredicateRecord = \ np  -> { cn = NONE; vp = mkVP np;  ap = NONE; adv = NONE };   
    mkPredicate : V -> PredicateRecord = \ v   -> { cn = NONE; vp = mkVP v;   ap = NONE; adv = NONE };
    mkPredicate : N -> Bool -> PredicateRecord = \ n,_ -> { cn = mkCN n; vp = NONE; ap = NONE; adv = NONE };
    mkPredicate : CN -> VP -> AP -> Adv -> PredicateRecord = \ cn,vp,ap,adv -> { cn = cn; vp = vp; ap = ap; adv = adv };
    };

    mkRelation = overload {
    mkRelation : N2   -> Bool -> RelationRecord = \ n2,b   -> { n2 = n2;   v2 = NONE; a2 = NONE; prep = NONE; reverse = b };
    mkRelation : V2   -> Bool -> RelationRecord = \ v2,b   -> { n2 = NONE; v2 = v2;   a2 = NONE; prep = NONE; reverse = b };
    mkRelation : A2   -> Bool -> RelationRecord = \ a2,b   -> { n2 = NONE; v2 = NONE; a2 = a2;   prep = NONE; reverse = b };
    mkRelation : Prep -> Bool -> RelationRecord = \ prep,b -> { n2 = NONE; v2 = NONE; a2 = NONE; prep = prep; reverse = b };
    mkRelation : N    -> Bool -> RelationRecord = \ n,b    -> { n2 = makeN2 n; v2 = NONE; a2 = NONE; prep = NONE; reverse = b };
    mkRelation : V2   -> RelationRecord = \ v2   -> { n2 = NONE; v2 = v2;   a2 = NONE; prep = NONE; reverse = False };
    mkRelation : Prep -> RelationRecord = \ prep -> { n2 = NONE; v2 = NONE; a2 = NONE; prep = prep; reverse = False };
    mkRelation : N2 -> V2 -> A2 -> Prep -> RelationRecord = \ n2,v2,a2,prep -> { n2 = n2; v2 = v2; a2 = a2; prep = prep; reverse = False };
    };

    mkEntity = overload {
    mkEntity : NP -> EntityRecord = \ np -> { np = np };
    mkEntity : PN -> EntityRecord = \ pn -> { np = mkNP pn }; 
    mkEntity : CN -> EntityRecord = \ cn -> { np = mkNP the_Det cn };
    };


    basic_apply = overload { 
    basic_apply : PredicateRecord -> EntityRecord -> Cl = \ p,e -> 
                  variants { mkCl e.np p.vp; 
                             mkCl e.np (mkNP a_Det   p.cn);
                             mkCl e.np (mkNP the_Det p.cn);
                             mkCl e.np p.ap;
                             mkCl e.np p.adv;
                           };
 -- basic_apply subject relation object -> cl
    basic_apply : EntityRecord -> RelationRecord -> EntityRecord -> Cl = \ e1,r,e2 -> 
            case r.reverse of {
                 True  => variants { mkCl e2.np r.v2 e1.np;
                                     mkCl e1.np (passiveVP r.v2);
                                     mkCl e1.np (passiveVP r.v2 e2.np);
                                     mkCl e2.np (mkVP (mkNP the_Det (mkCN r.n2 e1.np))); -- e2 is the r.cn of e1
                                     mkCl (mkNP the_Det (mkCN r.n2 e1.np)) (mkVP e2.np); -- the r.cn of e1 is e2
                                     mkCl e1.np have_V2 (mkNP a_Det (mkCN r.n2 e2.np));  -- if datatype property
                                     mkCl e2.np (mkVP (mkAP r.a2 e1.np));
                                     mkCl e2.np (mkVP (makeAdv r.prep e1.np));
                                   };
                 False => variants { mkCl e1.np (mkVP r.v2 e2.np);
                                     mkCl e2.np (passiveVP r.v2);
                                     mkCl e2.np (passiveVP r.v2 e1.np);
                                     mkCl e1.np (mkVP (mkCN r.n2 e2.np));
                                     mkCl e1.np (mkVP (mkAP r.a2 e2.np));
                                     mkCl e1.np (mkVP (makeAdv r.prep e2.np));
                                   } };

 -- False => basic_apply relation object  -> predicate  
 -- True  => basic_apply relation subject -> predicate

    basic_apply : RelationRecord -> EntityRecord -> PredicateRecord = \ r,e -> 
            case r.reverse of {
                 True  => { cn  = NONE; 
                            vp  = variants { passiveVP r.v2; 
                                             passiveVP r.v2 e.np }; 
                            ap  = NONE; 
                            adv = NONE;
                          };
                 False => { cn  = mkCN r.n2 e.np; 
                            vp  = mkVP r.v2 e.np; 
                            ap  = mkAP r.a2 e.np; 
                            adv = makeAdv r.prep e.np;
                          } };

 -- False => basic_apply object  relation -> predicate
 -- True  => basic_apply subject relation -> predicate                                                                                

    basic_apply : EntityRecord -> RelationRecord -> PredicateRecord = \ e,r -> 
            case r.reverse of {
                 False => { cn  = mkCN r.n2 e.np; 
                            vp  = mkVP r.v2 e.np; 
                            ap  = mkAP r.a2 e.np; 
                            adv = NONE;
                          };
                 True  => { cn  = NONE; 
                            vp  = variants { passiveVP r.v2; 
                                             passiveVP r.v2 e.np }; 
                            ap  = NONE; 
                            adv = makeAdv r.prep e.np;
                          } };
    };

    basic_closure : RelationRecord -> PredicateRecord = \ r -> 
                          { cn  = mkCN r.n2; 
                            vp  = NONE; 
                            ap  = mkAP r.a2; 
                            adv = NONE;
                          };


    makeVP = overload {
    makeVP : RelationRecord -> EntityRecord -> VP = \ r,e ->
             variants { mkVP r.v2 e.np;
                        mkVP (mkCN r.n2 e.np); 
                        mkVP (mkAP r.a2 e.np);
                        mkVP (makeAdv r.prep e.np) };
    makeVP : PredicateRecord -> VP = \ p ->
             variants { p.vp;
                        mkVP p.cn;
                        mkVP (mkNP the_Det p.cn);
                        mkVP p.ap;
                        mkVP p.adv };
    };

    makeNP : CN -> NP = \ cn -> variants { mkNP a_Det cn; mkNP the_Det cn };

    makeConj : Conj -> EntityRecord -> PredicateRecord -> PredicateRecord -> S = \ c,e,p1,p2 -> 
            variants { mkS (mkCl e.np (mkAP    c p1.ap  p2.ap));
                       mkS (mkCl e.np (makeAdv c p1.adv p2.adv));
                       mkS (mkCl e.np (mkNP    c (makeNP p1.cn) (makeNP p2.cn)));
                       mkS c (mkS (mkCl e.np p1.vp)) (mkS (mkCl e.np p2.vp)) } ;

}
