abstract Chunk = Core, Clauses ** {

cat

  Chunk ;
  Chunks ;

fun

  OneChunk  : Chunk -> Chunks ;
  PlusChunk : Chunk -> Chunks -> Chunks ;

  ChunkPhr  : Chunks -> Phrase_Phr ;

  -- Core

  Class_Chunk     : Class -> Chunk;
  Entity_Chunk    : Entity -> Chunks;
  Predicate_Chunk : Predicate -> Chunk;
  Statement_Chunk : Statement -> Chunk; 

}