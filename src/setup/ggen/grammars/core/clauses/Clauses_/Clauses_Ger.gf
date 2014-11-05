--# -path=.:prelude:alltenses

concrete Clauses_Ger of Clauses_ = CoreGer ** Clauses_I with 

   (Basic  = BasicGer),
   (Syntax = SyntaxGer) ** { 

 oper 

     negPol : Pol = negativePol;

     mkIWouldLike = overload {
     mkIWouldLike : NP -> S = \ np -> mkS conditionalTense (mkCl i_NP (mkVP (mkVP have_V2 i) gern_Adv));
     mkIWouldLike : VP -> S = \ vp -> mkS (mkCl i_NP (mkVP (mkVP wuerden_VV p.vp) gern_Adv));
     };

     wuerden_VV : VV = mkVV (mkV "wuerden");
     gern_Adv   : Adv = mkAdv "gern";

}
