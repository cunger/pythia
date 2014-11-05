--# -path=.:prelude:alltenses

concrete Clauses_Eng of Clauses_ = CoreEng ** Clauses_I with 

   (Basic  = BasicEng),
   (Syntax = SyntaxEng) ** open ParadigmsEng, ExtraEng in {

 oper 

     negPol : Pol = UncNeg; -- for unconcatenated negation (e.g. "does not" instead of "doesn't")

     mkIWouldLike = overload {
     mkIWouldLike : NP -> S = \ np -> mkS conditionalTense (mkCl i_NP (mkVP like_V2 np));
     mkIWouldLike : VP -> S = \ vp -> mkS conditionalTense (mkCl i_NP (mkVP like_VV vp));
     };

     like_V2 : V2 = mkV2 (mkV "like");
     like_VV : VV = mkVV (mkV "like");

}
