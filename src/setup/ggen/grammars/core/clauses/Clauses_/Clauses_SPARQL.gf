--# -path=.:prelude

concrete Clauses_SPARQL of Clauses_ = Core_SPARQL ** open SPARQL in {


 lincat

    Modality = {};

    Clause_Cl      = Query;
    Sentence_S     = Query;
    Question_QS    = Query;
    Imperative_Imp = Query;

    WhPron = Ident;
    WhAdv  = Ident; 

 lin 

    clause p = p;

    ---- Declarative sentences ----

    present_pos c = c;
    present_neg c = c;
    {{#alltenses}}
    past_pos    c = c;
    past_neg    c = c;
    future_pos  c = c;
    future_neg  c = c;
    {{/alltenses}}

    ---- Modals ----

    mod_clause _ p = p;

    ---- Imperative ----

    imperative p = p;

    ---- Questions ---- 

    --queryAdv a p = mkQS (mkQCl a (mkClause p));
    --query   wh p = variants { mkQS (mkQCl wh p.vp);
    --                          mkQS (mkQCl wh (mkClSlash p.np p.vpSlash)) };

    --YesNo p = mkQS (mkClause p);

    ---- wh-forms ----

    Who   = mkIdent newVar emptyQuery;
    What  = mkIdent newVar emptyQuery;
    --Where = mkIdent newVar emptyQuery;
    --When  = when_IAdv;
    --How   = how_IAdv;
    --Why   = why_IAdv;

    Which   c = let v = newVar in mkIdent True       v (mkQuery (mkTriple v rdf_type c));
    HowMany c = let v = newVar in mkIdent True Count v (mkQuery (mkTriple v rdf_type c));

}
