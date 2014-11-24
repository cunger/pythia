incomplete concrete ChunkI of Chunk = Core, Clauses ** open Basic, Syntax, Prelude in {

lincat

  Chunk  = {s : Str};
  Chunks = {s : Str};

lin

  OneChunk  c    = c ;
  PlusChunk c cs = cc2 c cs ;

  ChunkPhr c = lin Phr (ss ("*" ++ c.s)) | lin Phr c ;

  -- Core

  Class_Chunk     c = mkUtt c.cn | mkUtt c.vp | mkUtt c.ap | mkUtt c.adv;
  Entity_Chunk    e = mkUtt e.np;
  Predicate_Chunk p = mkUtt p.cn | mkUtt p.vp | mkUtt p.ap | mkUtt p.adv;
  Statement_Chunk s = mkUtt s; 

}