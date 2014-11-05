--# -path=.:prelude:alltenses

incomplete concrete DialogI of Dialog = open Basic, Syntax in {


 lincat 

    DialogAct = Utt;


 lin

    ---- Dialog acts ----

    Request i = i;
    Provide i = i;


    -- Customer service

    --Need   d _ i = mkCl d.np (mkVP need_V2 i.np);
    --Want   d _ i = mkCl d.np (mkVP want_V2 i.np);
    --               -- {{#alltenses}}mkIWouldLike i.np;{{/alltenses}}
    --WantTo d   p = mkCl d.np (mkVP want_VV p.vp);
    --               -- {{#alltenses}}mkIWouldLike p.vp;{{/alltenses}}

    DoYouHave_Class      _ c = mkUtt (mkQS (mkCl you_NP (mkVP have_V2 (mkNP aPl_Det c.cn))));
    DoYouHave_Individual _ i = mkUtt (mkQS (mkCl you_NP (mkVP have_V2 i.np)));


}
