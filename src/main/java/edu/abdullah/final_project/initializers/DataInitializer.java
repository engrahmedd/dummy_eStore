package edu.abdullah.final_project.initializers;

import edu.abdullah.final_project.entities.Manufacturer;
import edu.abdullah.final_project.entities.Product;
import edu.abdullah.final_project.entities.Role;
import edu.abdullah.final_project.entities.User;
import edu.abdullah.final_project.enums.Category;
import edu.abdullah.final_project.enums.Difficulty;
import edu.abdullah.final_project.enums.RoleName;
import edu.abdullah.final_project.repositories.ManufacturerRepository;
import edu.abdullah.final_project.repositories.ProductRepository;
import edu.abdullah.final_project.repositories.RoleRepository;
import edu.abdullah.final_project.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           ManufacturerRepository manufacturerRepository,
                           ProductRepository productRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
        seedUsers();
        seedManufacturers();
        seedProducts();
    }

    private void seedRoles() {
        roleRepository.findByName(RoleName.CUSTOMER)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.CUSTOMER)));
        roleRepository.findByName(RoleName.ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(RoleName.ADMIN)));
    }

    private void seedUsers() {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN).orElseThrow();
        Role customerRole = roleRepository.findByName(RoleName.CUSTOMER).orElseThrow();

        if (userRepository.findByEmail("admin.user@gmail.com").isEmpty()) {
            User admin = new User();
            admin.setEmail("admin.user@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("customer@test.com").isEmpty()) {
            User customer = new User();
            customer.setEmail("customer@test.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setFirstName("Test");
            customer.setLastName("Customer");
            customer.setRoles(Set.of(customerRole));
            userRepository.save(customer);
        }
    }

    private void seedManufacturers() {
        List<Object[]> data = List.of(
                new Object[]{"TimberTech Models",    "123 Maple St, Denver, CO 80202",         "303-555-0141", "contact@timbertechmodels.com"},
                new Object[]{"Ironwood Creations",   "456 Oak Ave, Chicago, IL 60607",          "312-555-0182", "info@ironwoodcreations.com"},
                new Object[]{"NordicCraft",          "789 Fjord Rd, Seattle, WA 98101",         "206-555-0193", "support@nordiccraft.com"},
                new Object[]{"Orbital Labs",         "101 Space Park Dr, Houston, TX 77058",    "713-555-0114", "hello@orbitallabs.com"},
                new Object[]{"ArcaneWorks",          "202 Mystic Ln, Salem, MA 01970",          "978-555-0155", "sales@arcaneworks.com"},
                new Object[]{"Skyline Structures",   "303 Tower Blvd, New York, NY 10001",      "212-555-0166", "info@skylinestructures.com"},
                new Object[]{"LumaScenes",           "404 Light St, San Diego, CA 92101",       "619-555-0177", "contact@lumascenes.com"}
        );

        for (Object[] row : data) {
            String name = (String) row[0];
            manufacturerRepository.findByName(name)
                    .orElseGet(() -> manufacturerRepository.save(
                            new Manufacturer(name, (String) row[1], (String) row[2], (String) row[3])
                    ));
        }
    }

    private void seedProducts() {
        if (productRepository.count() > 0) return;

        List<Object[]> data = List.of(
                new Object[]{"P1001", "Luminous Globe 3D Wooden Puzzle",              "TimberTech Models",  180, Difficulty.MEDIUM, Category.MECHANICAL,    new BigDecimal("59.99"), new BigDecimal("28.00"), "Rotating wooden globe with gears and warm LED lighting for display.",               "A beautifully engineered wooden globe featuring a visible gear system that powers smooth rotation. The integrated LED lighting adds a warm ambient glow, making it both a mechanical marvel and a decorative centerpiece for any room."},
                new Object[]{"P1002", "Steam Engine Mechanical 3D Wooden Puzzle",     "Ironwood Creations", 320, Difficulty.HARD,   Category.MECHANICAL,    new BigDecimal("64.99"), new BigDecimal("31.00"), "Detailed steam engine model with moving pistons and crankshaft parts.",              "This detailed wooden model recreates the power of a vintage steam locomotive with fully visible pistons and crankshaft movement. Designed for advanced builders, it offers a rewarding assembly experience and a striking mechanical display."},
                new Object[]{"P1003", "Pendulum Clock Wooden Mechanical Puzzle",      "Skyline Structures", 210, Difficulty.MEDIUM, Category.MECHANICAL,    new BigDecimal("54.99"), new BigDecimal("26.00"), "Functional wooden clock featuring exposed gears and moving parts.",                  "A precision-crafted wooden clock that showcases its inner workings through exposed gears. Combining functionality with craftsmanship, this model provides both a practical timepiece and an engaging mechanical build."},
                new Object[]{"P1004", "Skyrail Marble Run",                           "TimberTech Models",  260, Difficulty.MEDIUM, Category.MECHANICAL,    new BigDecimal("49.99"), new BigDecimal("24.00"), "Hand-cranked marble run with continuous motion and looping tracks.",                 "A dynamic marble run with hand-crank operation that sends marbles through loops, ramps, and spirals. The exposed mechanics make it as satisfying to watch as it is to build."},
                new Object[]{"P1005", "Medieval Castle 3D Wooden Puzzle",             "NordicCraft",        350, Difficulty.HARD,   Category.HISTORICAL,    new BigDecimal("69.99"), new BigDecimal("33.00"), "Detailed medieval castle with towers, gates, and working drawbridge.",               "Reconstruct a fortress from the Middle Ages with this intricate wooden model. Features articulated gates, layered towers, and a functioning drawbridge mechanism that evokes the grandeur of medieval architecture."},
                new Object[]{"P1006", "Egyptian Pyramid Architecture Puzzle",         "Skyline Structures", 190, Difficulty.MEDIUM, Category.HISTORICAL,    new BigDecimal("44.99"), new BigDecimal("21.00"), "Replica pyramid with internal chamber details and hieroglyph panels.",               "Explore ancient Egypt through this architectural model of the great pyramid. Internal chamber cutaways reveal hidden rooms while decorative panels display recreated hieroglyphs for an immersive historical build."},
                new Object[]{"P1007", "Roman Colosseum Wooden Puzzle",                "Ironwood Creations", 420, Difficulty.HARD,   Category.HISTORICAL,    new BigDecimal("74.99"), new BigDecimal("36.00"), "Large-scale Colosseum replica with arched tiers and arena floor detail.",            "An ambitious recreation of Rome's iconic amphitheater. Multi-tiered arched walls and a detailed arena floor make this a showcase piece for history enthusiasts and advanced puzzle builders alike."},
                new Object[]{"P1008", "Viking Longship Wooden Puzzle",                "NordicCraft",        230, Difficulty.MEDIUM, Category.NAUTICAL,      new BigDecimal("54.99"), new BigDecimal("26.00"), "Detailed Viking ship with carved shields, oars, and dragon prow.",                   "Set sail with this finely detailed Viking longship, complete with decorative shields along the hull, functional oars, and a carved dragon figurehead at the prow. A proud centerpiece for any display shelf."},
                new Object[]{"P1009", "Pirate Ship 3D Wooden Puzzle",                 "Ironwood Creations", 370, Difficulty.HARD,   Category.NAUTICAL,      new BigDecimal("67.99"), new BigDecimal("32.00"), "Three-masted pirate vessel with rigging, cannons, and skull flag.",                  "A swashbuckling three-masted pirate ship loaded with detail: working rigging, side-mounted cannons, and a jolly roger flag. Designed for experienced builders who want a dramatic nautical centerpiece."},
                new Object[]{"P1010", "Submarine Mechanical Puzzle",                  "Orbital Labs",       280, Difficulty.MEDIUM, Category.NAUTICAL,      new BigDecimal("59.99"), new BigDecimal("28.00"), "Cold War-era submarine model with rotating periscope and hatch details.",            "Dive deep into mechanical assembly with this Cold War-inspired submarine model. A rotating periscope, detailed hatch covers, and realistic hull plating bring underwater exploration to your desktop."},
                new Object[]{"P1011", "Space Station Orbital Puzzle",                 "Orbital Labs",       310, Difficulty.HARD,   Category.SCI_FI,        new BigDecimal("69.99"), new BigDecimal("33.00"), "Modular space station with rotating solar panels and docking rings.",                "Build your own orbital outpost with this modular space station kit. Solar panel arrays rotate on their mounts, docking rings snap together cleanly, and the whole structure assembles into an impressive sci-fi centerpiece."},
                new Object[]{"P1012", "Lunar Rover Exploration Vehicle",              "Orbital Labs",       150, Difficulty.EASY,   Category.SCI_FI,        new BigDecimal("39.99"), new BigDecimal("19.00"), "Moon buggy with articulated suspension and rotating antenna dish.",                 "An approachable build for beginners, this lunar rover features articulated suspension, spinning wheels, and a poseable antenna dish. A great introduction to 3D wooden assembly with a fun sci-fi theme."},
                new Object[]{"P1013", "Cyberpunk Mech Walker Puzzle",                 "Orbital Labs",       340, Difficulty.HARD,   Category.SCI_FI,        new BigDecimal("72.99"), new BigDecimal("35.00"), "Bipedal mech with articulated arms, cockpit canopy, and weapon mounts.",             "Step into the future with this imposing bipedal mech. Articulated arms move independently, a hinged cockpit canopy opens to reveal the pilot bay, and interchangeable weapon mounts let you customise your build."},
                new Object[]{"P1014", "Dragon Skull Trophy Puzzle",                   "ArcaneWorks",        270, Difficulty.MEDIUM, Category.FANTASY,       new BigDecimal("57.99"), new BigDecimal("27.00"), "Oversized dragon skull with articulated jaw and engraved scale texture.",            "An imposing fantasy trophy piece featuring a massive dragon skull with a hinged articulated jaw and hand-engraved scale texture across the surface. Display it on a shelf or desk as a bold statement piece."},
                new Object[]{"P1015", "Enchanted Treehouse Puzzle",                   "ArcaneWorks",        390, Difficulty.HARD,   Category.FANTASY,       new BigDecimal("71.99"), new BigDecimal("34.00"), "Multi-level fantasy treehouse with rope bridges and hidden compartments.",           "A whimsical multi-storey treehouse nestled among sculpted branches. Rope bridge connectors link platforms at different heights, and hidden compartments are tucked throughout for a delightful discovery experience."},
                new Object[]{"P1016", "Wizard Tower Wooden Puzzle",                   "ArcaneWorks",        220, Difficulty.MEDIUM, Category.FANTASY,       new BigDecimal("52.99"), new BigDecimal("25.00"), "Tall fantasy tower with spiral staircase, telescope, and spell room.",               "Ascend this tall wooden wizard tower floor by floor, assembling a spiral staircase, a rooftop telescope, and a fully detailed spell room filled with tiny accessories. A rewarding build for fantasy fans."},
                new Object[]{"P1017", "Empire State Building Puzzle",                 "Skyline Structures", 240, Difficulty.MEDIUM, Category.ARCHITECTURE,  new BigDecimal("54.99"), new BigDecimal("26.00"), "Iconic New York skyscraper with tiered setbacks and art deco spire.",               "Recreate one of the world's most recognisable skylines with this faithful wooden replica of the Empire State Building. Tiered setbacks and a detailed art deco spire capture the elegance of 1930s New York."},
                new Object[]{"P1018", "Eiffel Tower Deluxe Wooden Puzzle",            "Skyline Structures", 200, Difficulty.MEDIUM, Category.ARCHITECTURE,  new BigDecimal("49.99"), new BigDecimal("24.00"), "Intricate lattice tower with observation deck and base arch detail.",                "A deluxe recreation of Paris's beloved iron tower in precision-cut wood. The intricate lattice framework, observation deck, and four-arch base come together into a striking display of architectural engineering."},
                new Object[]{"P1019", "Japanese Pagoda Temple Puzzle",                "NordicCraft",        290, Difficulty.HARD,   Category.ARCHITECTURE,  new BigDecimal("64.99"), new BigDecimal("31.00"), "Five-tiered pagoda with upturned rooflines and decorative lantern details.",         "A serene five-tiered pagoda featuring traditionally upturned rooflines at each level and decorative hanging lanterns. This challenging build rewards patience with a beautifully balanced architectural centrepiece."},
                new Object[]{"P1020", "Notre Dame Cathedral Puzzle",                  "Skyline Structures", 380, Difficulty.HARD,   Category.ARCHITECTURE,  new BigDecimal("72.99"), new BigDecimal("35.00"), "Gothic cathedral with flying buttresses, rose window, and twin towers.",             "Honour one of Europe's great Gothic cathedrals with this intricate wooden model. Flying buttresses, a detailed rose window, and twin bell towers faithfully recreate the majesty of Notre Dame de Paris."},
                new Object[]{"P1021", "Celestial Orrery Puzzle",                      "LumaScenes",         300, Difficulty.HARD,   Category.DECORATIVE,    new BigDecimal("69.99"), new BigDecimal("33.00"), "Mechanical orrery showing planetary orbits with brass-finish gear rings.",           "An elegant mechanical orrery that models the orbits of the inner planets. Interlocking gear rings with a brass-effect finish rotate in sequence, making this both a functional curiosity and a refined decorative object."},
                new Object[]{"P1022", "Vintage Gramophone Puzzle",                    "LumaScenes",         170, Difficulty.EASY,   Category.DECORATIVE,    new BigDecimal("42.99"), new BigDecimal("20.00"), "Classic horn gramophone with rotating turntable and tonearm detail.",               "A nostalgic recreation of the classic wind-up gramophone. The turntable rotates smoothly and the tonearm pivots into position, capturing the charm of early recorded music in a compact decorative model."},
                new Object[]{"P1023", "Steampunk Airship Puzzle",                     "ArcaneWorks",        330, Difficulty.HARD,   Category.DECORATIVE,    new BigDecimal("67.99"), new BigDecimal("32.00"), "Fantasy airship with rotating propellers, balloon envelope, and gondola.",           "Take to the skies with this fantastical steampunk airship. Rotating propellers, a detailed balloon envelope, and a fully outfitted gondola cabin combine Victorian aesthetic with imaginative mechanical design."},
                new Object[]{"P1024", "Butterfly Garden Music Box",                   "LumaScenes",         140, Difficulty.EASY,   Category.DECORATIVE,    new BigDecimal("39.99"), new BigDecimal("19.00"), "Delicate music box with pop-up butterflies and wind-up melody mechanism.",           "A charming and delicate music box that opens to reveal pop-up butterflies on spring mounts. The wind-up mechanism plays a gentle melody, making this an ideal gift or a soft decorative accent for any space."},
                new Object[]{"P1025", "Sailing Yacht Wooden Puzzle",                  "NordicCraft",        195, Difficulty.MEDIUM, Category.NAUTICAL,      new BigDecimal("52.99"), new BigDecimal("25.00"), "Modern racing yacht with billowing sails and detailed deck fittings.",               "A sleek modern racing yacht with fabric-effect sails, detailed deck hardware, and a streamlined hull. This mid-difficulty build rewards the assembler with a graceful nautical display piece."},
                new Object[]{"P1026", "Antique Compass Rose Puzzle",                  "LumaScenes",         120, Difficulty.EASY,   Category.DECORATIVE,    new BigDecimal("41.99"), new BigDecimal("20.00"), "Decorative compass rose wall piece with layered directional points.",                "A layered decorative compass rose designed to be mounted as a wall piece or displayed flat. Concentric rings and pointed directional arms interlock cleanly, producing a refined navigational art object."},
                new Object[]{"P1027", "Ancient Greek Temple Puzzle",                  "Ironwood Creations", 310, Difficulty.HARD,   Category.HISTORICAL,    new BigDecimal("66.99"), new BigDecimal("32.00"), "Parthenon-style temple with fluted columns and detailed pediment carvings.",         "Reconstruct the glory of ancient Greece with this Parthenon-inspired temple. Fluted Doric columns, a triangular pediment with carved relief detail, and a raised stylobate base produce a classically proportioned display model."},
                new Object[]{"P1028", "Mayan Calendar Disc Puzzle",                   "ArcaneWorks",        160, Difficulty.EASY,   Category.HISTORICAL,    new BigDecimal("43.99"), new BigDecimal("21.00"), "Circular calendar disc with concentric ring glyphs and centre sun face.",            "A faithful recreation of the Aztec sun stone in precision-cut wood. Concentric rings of calendar glyphs surround a detailed central sun face, producing an impressive wall piece or display medallion."},
                new Object[]{"P1029", "Alien Observatory Puzzle",                     "Orbital Labs",       250, Difficulty.MEDIUM, Category.SCI_FI,        new BigDecimal("58.99"), new BigDecimal("28.00"), "Extraterrestrial research station with rotating telescope dome and ramp.",            "An imaginative extraterrestrial research outpost featuring a rotating observatory dome, telescoping antenna array, and a deployable access ramp. A creative sci-fi build with plenty of poseable details."},
                new Object[]{"P1030", "Haunted Mansion Puzzle",                       "ArcaneWorks",        400, Difficulty.HARD,   Category.FANTASY,       new BigDecimal("73.99"), new BigDecimal("35.00"), "Spooky multi-storey mansion with secret passages and graveyard base.",               "A gloriously spooky haunted mansion spread across multiple storeys. Hidden passages connect rooms unexpectedly, a detachable graveyard base surrounds the foundation, and eerie architectural details reward close inspection."}
        );

        for (Object[] row : data) {
            String code        = (String) row[0];
            String name        = (String) row[1];
            String mfrName     = (String) row[2];
            Integer pieces     = (Integer) row[3];
            Difficulty diff    = (Difficulty) row[4];
            Category cat       = (Category) row[5];
            BigDecimal price   = (BigDecimal) row[6];
            BigDecimal wholesale = (BigDecimal) row[7];
            String shortDesc   = (String) row[8];
            String longDesc    = (String) row[9];

            Manufacturer manufacturer = manufacturerRepository.findByName(mfrName).orElseThrow();

            productRepository.findByProductCode(code).orElseGet(() -> {
                Product p = new Product();
                p.setProductCode(code);
                p.setName(name);
                p.setManufacturer(manufacturer);
                p.setPieceCount(pieces);
                p.setDifficulty(diff);
                p.setCategory(cat);
                p.setPrice(price);
                p.setWholesaleCost(wholesale);
                p.setShortDescription(shortDesc);
                p.setLongDescription(longDesc);
                p.setAcquiredDate(LocalDate.now());
                p.setActive(true);
                return productRepository.save(p);
            });
        }
    }
}