import java.util.LinkedList;

public class Part2 {
    public static void main(String[] args) {
        InputParser ip = new InputParser("input");
        Disk d = new Disk(ip);
        d.compress();
        System.out.println("\n" + d.checksum());
    }

    public static class Disk {
        private static class Block {
            public final int ID;
            public final int SIZE;
            public int space; // Utrymme till nästa block
    
            public Block(int id, int size, int space) {
                ID = id;
                this.SIZE = size;
                this.space = space;
            }
        }

        private final LinkedList<Block> DISK = new LinkedList<>();

        public Disk(InputParser ip) {
            String data = ip.getLine(0);
            for (int i = 0; i < data.length(); i+=2) {
                int id = i/2;
                int size = Integer.parseInt(data.charAt(i) + "");
                int space = i!=data.length()-1 ? Integer.parseInt(data.charAt(i+1) + "") : 0;
                DISK.add(new Block(id, size, space));
            }
        }

        public void compress() {
            System.out.println("Compressing...");

            LinkedList<Block> processingQueue = new LinkedList<>();
            for (int i = DISK.size()-1; i >= 1; i--)
                processingQueue.add(DISK.get(i));

            int searchStart = 0;
            for (Block dataBlock : processingQueue) {
                int dataBlockIndex = DISK.indexOf(dataBlock);
                // Hitta det första tomma utrymme där datablocket får plats (LÅNGSAMT SOM FAN!!!)
                for (int i = searchStart; i < dataBlockIndex; i++) {
                    Block otherBlock = DISK.get(i);
                    if (otherBlock.space >= dataBlock.SIZE) {
                        // Ta bort blocket från slutet av disken
                        DISK.get(dataBlockIndex-1).space += dataBlock.SIZE + dataBlock.space;
                        DISK.remove(dataBlock);
                        // Sätt in blocket i det fria utrymmet i otherBlock
                        dataBlock.space = otherBlock.space - dataBlock.SIZE;
                        otherBlock.space = 0;
                        DISK.add(i+1, dataBlock);
                        break;
                    } else if (i == searchStart && otherBlock.space == 0) {
                        // Flytta sökstartpunkten för att minska antalet fullt ockuperade platser som söks igenom
                        searchStart++;
                        continue;
                    }
                }

                ProgressBar.progressBar(DISK.size() - dataBlock.ID, DISK.size()-1, true);
            }            
        }

        public long checksum() {
            long sum = 0;
            int index = 0;
            for (Block block : DISK) {
                for (int i = 0; i < block.SIZE; i++) {
                    sum += block.ID * index++;
                }
                index += block.space;
            }
            return sum;
        }

        public void print() {
            System.out.println();
            for (Block block : DISK) {
                for (int i = 0; i < block.SIZE; i++)
                    System.out.print(block.ID);
                for (int i = 0; i < block.space; i++)
                    System.out.print('.');
            }
        }
    }
}
