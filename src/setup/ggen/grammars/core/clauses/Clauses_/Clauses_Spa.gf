--# -path=.:prelude:alltenses

concrete Clauses_Spa of Clauses_ = CoreSpa ** Clauses_I with 

   (Basic  = BasicSpa),
   (Syntax = SyntaxSpa) ** open ParadigmsSpa, ExtraSpa in {

 oper 

     negPol : Pol = negativePol; 

     mkIWouldLike = overload {
     mkIWouldLike : NP -> S = \ np -> mkS conditionalTense (mkCl i_NP (mkVP like_V2 np));
     mkIWouldLike : VP -> S = \ vp -> mkS conditionalTense (mkCl i_NP (mkVP like_VV vp));
     };

     like_V2 : V2 = mkV2 (mkV "querer");
     like_VV : VV = mkVV (mkV "querer");

}
