package com.cpe.springboot.card.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpe.springboot.card.model.CardDTO;
import com.cpe.springboot.card.model.CardModel;
import com.cpe.springboot.card.model.CardReference;
import com.cpe.springboot.common.tools.DTOMapper;
import com.cpe.springboot.user.model.UserModel;

@Service
public class CardModelService {
	private final CardModelRepository cardRepository;
	private final CardReferenceService cardRefService;
	private Random rand;

	private static final float[] POSSIBLE_ATTACKS = {10, 20, 30, 40, 50, 60, 70, 80, 90, 100};
    private static final float[] POSSIBLE_DEFENSE = {15, 25, 35, 45, 55, 65, 75, 85, 95};
    private static final float[] POSSIBLE_HP = {20, 30, 40, 50, 60, 70, 80, 90, 100};

	public CardModelService(CardModelRepository cardRepository,CardReferenceService cardRefService) {
		this.rand=new Random();
		// Dependencies injection by constructor
		this.cardRepository=cardRepository;
		this.cardRefService=cardRefService;
	}
	
	public List<CardModel> getAllCardModel() {
		List<CardModel> cardList = new ArrayList<>();
		cardRepository.findAll().forEach(cardList::add);
		return cardList;
	}

	public CardDTO addCard(CardModel cardModel) {
		CardModel cDb=cardRepository.save(cardModel);
		return DTOMapper.fromCardModelToCardDTO(cDb);
	}

	public void updateCardRef(CardModel cardModel) {
		cardRepository.save(cardModel);

	}
	public CardDTO updateCard(CardModel cardModel) {
		CardModel cDb=cardRepository.save(cardModel);
		return DTOMapper.fromCardModelToCardDTO(cDb);
	}
	public Optional<CardModel> getCard(Integer id) {
		return cardRepository.findById(id);
	}
	
	public void deleteCardModel(Integer id) {
		cardRepository.deleteById(id);
	}
	
	public List<CardModel> getRandCard(int nbr){
		List<CardModel> cardList=new ArrayList<>();
		for(int i=0;i<nbr;i++) {
			CardReference currentCardRef=cardRefService.getRandCardRef();
			CardModel currentCard=new CardModel(currentCardRef);
			currentCard.setAttack(POSSIBLE_ATTACKS[rand.nextInt(POSSIBLE_ATTACKS.length)]);
            currentCard.setDefence(POSSIBLE_DEFENSE[rand.nextInt(POSSIBLE_DEFENSE.length)]);
            currentCard.setHp(POSSIBLE_HP[rand.nextInt(POSSIBLE_HP.length)]);
            
            // Calcul de l'énergie basé sur l'attaque
            currentCard.setEnergy(calculateCardEnergy(currentCard.getAttack()));
            
            // Calcul du prix basé sur les statistiques
            currentCard.setPrice(currentCard.computePrice());
			//save new card before sending for user creation
			//this.addCard(currentCard);
			cardList.add(currentCard);
		}
		return cardList;
	}

	private int calculateCardEnergy(float attack) {
        if (attack < 40) return 1;
        if (attack < 70) return 2;
        return 3;
    }


	public List<CardModel> getAllCardToSell(){
		return this.cardRepository.findByUser(null);
	}
}

