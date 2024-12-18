package employee.management.system.service;

import employee.management.system.entity.Position;
import employee.management.system.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService{

    private final PositionRepository positionRepository;

    @Autowired
    public PositionServiceImpl(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Override
    public Position findPositionById(int id) {
        return positionRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Position with id: " + id + " not found"));
    }

    @Override
    public List<Position> findAllPositions() {
        return positionRepository.findAll();
    }

    @Transactional
    @Override
    public void addPosition(Position position) {
        positionRepository.save(position);
    }

    @Transactional
    @Override
    public void updatePosition(Position position) {
        positionRepository.save(position);
    }

    @Transactional
    @Override
    public void deletePositionById(int id) {
        if(!positionRepository.existsById(id)){
            throw new RuntimeException("Position with id: " + id + " not found");
        }
        positionRepository.deleteById(id);
    }

    @Override
    public Position findPositionByName(String name) {
        return positionRepository.findPositionByPositionName(name);
    }
}
