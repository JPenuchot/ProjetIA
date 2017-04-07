package memo;

import structure.PlateauFouFou;
import structure.State;
import structure.StateUtils;

public class MemoAlphaBeta{
	public MemoAlphaBeta(PlateauFouFou plat){
		this.listBlackMoves = null;
		this.listWhiteMoves = null;
	}

	public static int stateToInt(State stState){
        switch (stState){
            case black:
                return 0;
            case white:
                return 1;
            case empty:
                return 2;
        }
        return -1;
    }

    public String[] listBlackMoves = null;
    public String[] listWhiteMoves = null;

    public float heurValWhite = 0.f;
    public float heurValBlack = 0.f;

    public int numberStateBlack = 0;
    public int numberStateWhite = 0;
}